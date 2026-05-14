// ===== API BASE URL =====
const API = '';

// ===== UTILITY FUNCTIONS =====
function formatCurrency(value) {
    return new Intl.NumberFormat('pt-BR', {
        style: 'currency',
        currency: 'BRL'
    }).format(value);
}

function formatNumber(value, decimals = 0) {
    return new Intl.NumberFormat('pt-BR', {
        minimumFractionDigits: decimals,
        maximumFractionDigits: decimals
    }).format(value);
}

function formatDateTime(isoString) {
    if (!isoString) return '—';
    const date = new Date(isoString);
    return date.toLocaleString('pt-BR', {
        day: '2-digit',
        month: '2-digit',
        year: 'numeric',
        hour: '2-digit',
        minute: '2-digit'
    });
}

function formatWeight(kg) {
    if (kg >= 1000) {
        return formatNumber(kg / 1000, 2) + ' ton';
    }
    return formatNumber(kg, 1) + ' kg';
}

function showToast(message, isError = false) {
    const existing = document.querySelector('.toast');
    if (existing) existing.remove();

    const toast = document.createElement('div');
    toast.className = `toast ${isError ? 'toast--error' : ''}`;
    toast.innerHTML = `
        <span class="toast-icon">${isError ? '⚠' : '✓'}</span>
        <span>${message}</span>
    `;
    document.body.appendChild(toast);
    setTimeout(() => toast.remove(), 3000);
}

async function fetchAPI(endpoint) {
    try {
        const response = await fetch(API + endpoint);
        if (!response.ok) throw new Error(`HTTP ${response.status}`);
        return await response.json();
    } catch (error) {
        console.error(`Erro ao buscar ${endpoint}:`, error);
        showToast(`Erro ao carregar dados: ${error.message}`, true);
        return null;
    }
}

// ===== CHART CONFIG =====
const CHART_COLORS = ['#6366f1', '#10b981', '#f59e0b', '#f43f5e', '#06b6d4', '#8b5cf6', '#ec4899'];

Chart.defaults.color = '#94a3b8';
Chart.defaults.font.family = "'Inter', sans-serif";
Chart.defaults.font.size = 12;

let grainBarChartInstance = null;
let costDonutChartInstance = null;

// ===== NAVIGATION =====
const navItems = document.querySelectorAll('.nav-item[data-section]');
const sections = document.querySelectorAll('.section');

const sectionMeta = {
    overview: { title: 'Visão Geral', subtitle: 'Resumo operacional em tempo real' },
    weighings: { title: 'Pesagens', subtitle: 'Histórico e consulta de pesagens' },
    transactions: { title: 'Transações', subtitle: 'Transações de transporte ativas' },
    fleet: { title: 'Frota & Infraestrutura', subtitle: 'Caminhões, balanças e grãos cadastrados' }
};

navItems.forEach(item => {
    item.addEventListener('click', (e) => {
        e.preventDefault();
        const sectionId = item.dataset.section;

        navItems.forEach(n => n.classList.remove('active'));
        item.classList.add('active');

        sections.forEach(s => s.classList.remove('active'));
        const target = document.getElementById(`section-${sectionId}`);
        if (target) {
            target.classList.add('active');
        }

        const meta = sectionMeta[sectionId];
        if (meta) {
            document.getElementById('pageTitle').textContent = meta.title;
            document.getElementById('pageSubtitle').textContent = meta.subtitle;
        }

        // Close mobile sidebar
        document.getElementById('sidebar').classList.remove('open');
        const backdrop = document.querySelector('.sidebar-backdrop');
        if (backdrop) backdrop.classList.remove('active');
    });
});

// Mobile menu toggle
const menuToggle = document.getElementById('menuToggle');
menuToggle.addEventListener('click', () => {
    const sidebar = document.getElementById('sidebar');
    sidebar.classList.toggle('open');

    let backdrop = document.querySelector('.sidebar-backdrop');
    if (!backdrop) {
        backdrop = document.createElement('div');
        backdrop.className = 'sidebar-backdrop';
        document.body.appendChild(backdrop);
        backdrop.addEventListener('click', () => {
            sidebar.classList.remove('open');
            backdrop.classList.remove('active');
        });
    }
    backdrop.classList.toggle('active');
});

// ===== OVERVIEW SECTION =====
async function loadOverview() {
    const [grainSummary, transactions] = await Promise.all([
        fetchAPI('/api/reports/grain-summary'),
        fetchAPI('/api/transactions')
    ]);

    if (grainSummary) {
        renderKPIs(grainSummary, transactions);
        renderGrainSummaryTable(grainSummary);
        renderGrainBarChart(grainSummary);
        renderCostDonutChart(grainSummary);
    }
}

function renderKPIs(grainSummary, transactions) {
    const totalWeighings = grainSummary.reduce((sum, g) => sum + g.totalWeighings, 0);
    const totalWeight = grainSummary.reduce((sum, g) => sum + g.totalNetWeightKg, 0);
    const totalCost = grainSummary.reduce((sum, g) => sum + parseFloat(g.totalCost), 0);

    const activeTransactions = transactions
        ? transactions.filter(t => t.status === 'IN_TRANSIT' || t.status === 'WEIGHING').length
        : 0;

    animateValue('kpiTotalWeighingsValue', totalWeighings, false);
    document.getElementById('kpiTotalWeightValue').textContent = formatWeight(totalWeight);
    document.getElementById('kpiTotalRevenueValue').textContent = formatCurrency(totalCost);
    animateValue('kpiActiveTransactionsValue', activeTransactions, false);
}

function animateValue(elementId, endValue, isCurrency = false) {
    const el = document.getElementById(elementId);
    const duration = 600;
    const start = performance.now();
    const startVal = 0;

    function update(now) {
        const elapsed = now - start;
        const progress = Math.min(elapsed / duration, 1);
        const eased = 1 - Math.pow(1 - progress, 3); // ease-out cubic
        const current = Math.round(startVal + (endValue - startVal) * eased);
        el.textContent = isCurrency ? formatCurrency(current) : formatNumber(current);
        if (progress < 1) requestAnimationFrame(update);
    }
    requestAnimationFrame(update);
}

function renderGrainSummaryTable(data) {
    const tbody = document.getElementById('grainSummaryBody');
    if (!data || data.length === 0) {
        tbody.innerHTML = '<tr><td colspan="6" class="table-empty">Nenhuma pesagem registrada</td></tr>';
        return;
    }

    tbody.innerHTML = data.map(g => `
        <tr>
            <td style="color: var(--text-primary); font-weight: 600;">${g.grainType}</td>
            <td>${formatNumber(g.totalWeighings)}</td>
            <td>${formatNumber(g.totalNetWeightKg, 1)}</td>
            <td>${formatNumber(g.totalNetWeightKg / 1000, 3)}</td>
            <td style="color: var(--emerald-400); font-weight: 600;">${formatCurrency(g.totalCost)}</td>
            <td>${g.totalWeighings > 0 ? formatCurrency(g.totalCost / g.totalWeighings) : '—'}</td>
        </tr>
    `).join('');
}

function renderGrainBarChart(data) {
    document.getElementById('grainChartLoading').style.display = 'none';

    if (!data || data.length === 0) return;

    const ctx = document.getElementById('grainBarChart').getContext('2d');

    if (grainBarChartInstance) grainBarChartInstance.destroy();

    grainBarChartInstance = new Chart(ctx, {
        type: 'bar',
        data: {
            labels: data.map(g => g.grainType),
            datasets: [
                {
                    label: 'Peso Líquido (ton)',
                    data: data.map(g => (g.totalNetWeightKg / 1000).toFixed(2)),
                    backgroundColor: CHART_COLORS.map(c => c + '33'),
                    borderColor: CHART_COLORS,
                    borderWidth: 2,
                    borderRadius: 8,
                    borderSkipped: false,
                    barPercentage: 0.6,
                },
                {
                    label: 'Pesagens',
                    data: data.map(g => g.totalWeighings),
                    backgroundColor: 'rgba(99, 102, 241, 0.15)',
                    borderColor: '#818cf8',
                    borderWidth: 2,
                    borderRadius: 8,
                    borderSkipped: false,
                    barPercentage: 0.6,
                    yAxisID: 'y1',
                }
            ]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            interaction: {
                mode: 'index',
                intersect: false,
            },
            plugins: {
                legend: {
                    position: 'top',
                    align: 'end',
                    labels: {
                        usePointStyle: true,
                        pointStyle: 'rectRounded',
                        padding: 16,
                    }
                },
                tooltip: {
                    backgroundColor: '#1a1f35',
                    borderColor: 'rgba(148, 163, 184, 0.15)',
                    borderWidth: 1,
                    titleColor: '#f1f5f9',
                    bodyColor: '#94a3b8',
                    padding: 12,
                    cornerRadius: 8,
                }
            },
            scales: {
                x: {
                    grid: { display: false },
                    border: { display: false },
                    ticks: { padding: 8 },
                },
                y: {
                    position: 'left',
                    title: { display: true, text: 'Peso (ton)', color: '#64748b' },
                    grid: { color: 'rgba(148, 163, 184, 0.06)' },
                    border: { display: false },
                    ticks: { padding: 8 },
                },
                y1: {
                    position: 'right',
                    title: { display: true, text: 'Pesagens', color: '#64748b' },
                    grid: { display: false },
                    border: { display: false },
                    ticks: { padding: 8, stepSize: 1 },
                }
            }
        }
    });
}

function renderCostDonutChart(data) {
    document.getElementById('costChartLoading').style.display = 'none';

    if (!data || data.length === 0) return;

    const ctx = document.getElementById('costDonutChart').getContext('2d');

    if (costDonutChartInstance) costDonutChartInstance.destroy();

    costDonutChartInstance = new Chart(ctx, {
        type: 'doughnut',
        data: {
            labels: data.map(g => g.grainType),
            datasets: [{
                data: data.map(g => parseFloat(g.totalCost)),
                backgroundColor: CHART_COLORS.map(c => c + '88'),
                borderColor: CHART_COLORS,
                borderWidth: 2,
                hoverOffset: 8,
            }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            cutout: '65%',
            plugins: {
                legend: {
                    position: 'bottom',
                    labels: {
                        usePointStyle: true,
                        pointStyle: 'circle',
                        padding: 16,
                        generateLabels: function(chart) {
                            const data = chart.data;
                            const total = data.datasets[0].data.reduce((a, b) => a + b, 0);
                            return data.labels.map((label, i) => ({
                                text: `${label} (${((data.datasets[0].data[i] / total) * 100).toFixed(1)}%)`,
                                fillStyle: data.datasets[0].backgroundColor[i],
                                strokeStyle: data.datasets[0].borderColor[i],
                                lineWidth: 2,
                                pointStyle: 'circle',
                                index: i,
                            }));
                        }
                    }
                },
                tooltip: {
                    backgroundColor: '#1a1f35',
                    borderColor: 'rgba(148, 163, 184, 0.15)',
                    borderWidth: 1,
                    titleColor: '#f1f5f9',
                    bodyColor: '#94a3b8',
                    padding: 12,
                    cornerRadius: 8,
                    callbacks: {
                        label: function(ctx) {
                            return ` ${formatCurrency(ctx.raw)}`;
                        }
                    }
                }
            }
        }
    });
}

// ===== WEIGHINGS SECTION =====
async function searchWeighingsByDate() {
    const startInput = document.getElementById('filterStartDate').value;
    const endInput = document.getElementById('filterEndDate').value;

    if (!startInput || !endInput) {
        showToast('Preencha as datas de início e fim', true);
        return;
    }

    const start = new Date(startInput).toISOString();
    const end = new Date(endInput).toISOString();

    const data = await fetchAPI(`/api/reports/weighing-history?startDate=${start}&endDate=${end}`);
    renderWeighingsTable(data);
}

async function searchWeighingsByPlate() {
    const plate = document.getElementById('filterPlate').value.trim().toUpperCase();

    if (!plate) {
        showToast('Preencha a placa do caminhão', true);
        return;
    }

    const data = await fetchAPI(`/api/reports/weighing-history/plate/${plate}`);
    renderWeighingsTable(data);
}

function renderWeighingsTable(data) {
    const tbody = document.getElementById('weighingsBody');
    const countEl = document.getElementById('weighingCount');

    if (!data || data.length === 0) {
        tbody.innerHTML = '<tr><td colspan="8" class="table-empty">Nenhuma pesagem encontrada para os filtros selecionados</td></tr>';
        countEl.textContent = '0 resultados';
        return;
    }

    countEl.textContent = `${data.length} resultado${data.length > 1 ? 's' : ''}`;

    tbody.innerHTML = data.map(w => `
        <tr>
            <td>${formatDateTime(w.weighedAt)}</td>
            <td style="color: var(--text-primary); font-weight: 600;">${w.plate}</td>
            <td>${w.grainType}</td>
            <td>${formatNumber(w.grossWeight, 1)}</td>
            <td>${formatNumber(w.tare, 1)}</td>
            <td style="color: var(--blue-400); font-weight: 600;">${formatNumber(w.netWeight, 1)}</td>
            <td style="color: var(--emerald-400); font-weight: 600;">${formatCurrency(w.cargoCost)}</td>
            <td><code style="background: rgba(99,102,241,0.1); padding: 2px 8px; border-radius: 4px; font-size: 0.8rem; color: var(--blue-400);">${w.scaleId ? w.scaleId.substring(0, 8) + '...' : '—'}</code></td>
        </tr>
    `).join('');
}

// ===== TRANSACTIONS SECTION =====
async function loadTransactions() {
    const data = await fetchAPI('/api/transactions');
    renderTransactionsTable(data);
}

function getStatusBadge(status) {
    const map = {
        'IN_TRANSIT': { class: 'in-transit', label: 'Em Trânsito' },
        'WEIGHING': { class: 'weighing', label: 'Pesando' },
        'COMPLETED': { class: 'completed', label: 'Concluído' },
        'CANCELLED': { class: 'cancelled', label: 'Cancelado' }
    };
    const info = map[status] || { class: 'in-transit', label: status };
    return `<span class="status-badge status-badge--${info.class}">
                <span class="status-badge-dot"></span>
                ${info.label}
            </span>`;
}

function renderTransactionsTable(data) {
    const tbody = document.getElementById('transactionsBody');
    const countEl = document.getElementById('transactionCount');

    if (!data || data.length === 0) {
        tbody.innerHTML = '<tr><td colspan="6" class="table-empty">Nenhuma transação encontrada</td></tr>';
        countEl.textContent = '';
        return;
    }

    countEl.textContent = `${data.length} transação(ões)`;

    tbody.innerHTML = data.map(t => `
        <tr>
            <td><code style="background: rgba(99,102,241,0.1); padding: 2px 8px; border-radius: 4px; font-size: 0.78rem; color: var(--blue-400);">${t.id.substring(0, 8)}...</code></td>
            <td style="color: var(--text-primary); font-weight: 600;">${t.truckPlate || '—'}</td>
            <td>${t.grainTypeName || '—'}</td>
            <td>${getStatusBadge(t.status)}</td>
            <td>${formatDateTime(t.startedAt)}</td>
            <td>${t.finishedAt ? formatDateTime(t.finishedAt) : '—'}</td>
        </tr>
    `).join('');
}

// ===== FLEET SECTION =====
async function loadFleet() {
    const [trucks, scales, grainTypes] = await Promise.all([
        fetchAPI('/api/trucks'),
        fetchAPI('/api/scales'),
        fetchAPI('/api/grain-types')
    ]);

    renderTrucksTable(trucks);
    renderScalesTable(scales);
    renderGrainTypeCards(grainTypes);
}

function renderTrucksTable(data) {
    const tbody = document.getElementById('trucksBody');
    const countEl = document.getElementById('truckCount');

    if (!data || data.length === 0) {
        tbody.innerHTML = '<tr><td colspan="3" class="table-empty">Nenhum caminhão cadastrado</td></tr>';
        return;
    }

    countEl.textContent = `${data.length} caminhão(ões)`;

    tbody.innerHTML = data.map(t => `
        <tr>
            <td style="color: var(--text-primary); font-weight: 600;">${t.plate}</td>
            <td>${formatNumber(t.tare, 0)}</td>
            <td>${t.branchName || '—'}</td>
        </tr>
    `).join('');
}

function renderScalesTable(data) {
    const tbody = document.getElementById('scalesBody');

    if (!data || data.length === 0) {
        tbody.innerHTML = '<tr><td colspan="2" class="table-empty">Nenhuma balança cadastrada</td></tr>';
        return;
    }

    tbody.innerHTML = data.map(s => `
        <tr>
            <td style="color: var(--text-primary); font-weight: 600;">${s.code}</td>
            <td>${s.branchName || '—'}</td>
        </tr>
    `).join('');
}

function renderGrainTypeCards(data) {
    const container = document.getElementById('grainTypesCards');

    if (!data || data.length === 0) {
        container.innerHTML = '<span style="color: var(--text-muted);">Nenhum tipo de grão cadastrado</span>';
        return;
    }

    container.innerHTML = data.map((g, i) => `
        <div class="grain-type-card" style="--delay: ${i * 0.1}s;">
            <div class="grain-type-name">${g.name}</div>
            <div class="grain-type-price">
                ${formatCurrency(g.purchasePricePerTon)} <span>/ tonelada</span>
            </div>
        </div>
    `).join('');
}

// ===== SET DEFAULT DATES =====
function setDefaultDates() {
    const now = new Date();
    const monthAgo = new Date(now);
    monthAgo.setMonth(monthAgo.getMonth() - 1);

    const toLocalISO = (d) => {
        const pad = n => String(n).padStart(2, '0');
        return `${d.getFullYear()}-${pad(d.getMonth() + 1)}-${pad(d.getDate())}T${pad(d.getHours())}:${pad(d.getMinutes())}`;
    };

    document.getElementById('filterStartDate').value = toLocalISO(monthAgo);
    document.getElementById('filterEndDate').value = toLocalISO(now);
}

// Make search functions global (called from onclick in HTML)
window.searchWeighingsByDate = searchWeighingsByDate;
window.searchWeighingsByPlate = searchWeighingsByPlate;

// ===== INIT =====
document.addEventListener('DOMContentLoaded', () => {
    setDefaultDates();
    loadOverview();
    loadTransactions();
    loadFleet();
});
