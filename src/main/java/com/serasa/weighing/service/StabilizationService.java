package com.serasa.weighing.service;

import java.util.Optional;

public interface StabilizationService {

    /**
     * Adiciona uma leitura ao buffer e verifica se o peso estabilizou.
     *
     * @param scaleId identificador da balança
     * @param plate   placa do caminhão
     * @param weight  leitura bruta do peso
     * @return Optional com o peso estabilizado (média da janela) se estável, empty
     *         caso contrário
     */
    Optional<Double> addReadingAndCheck(String scaleId, String plate, double weight);
}
