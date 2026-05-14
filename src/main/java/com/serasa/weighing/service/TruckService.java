package com.serasa.weighing.service;

import com.serasa.weighing.dto.request.TruckRequest;
import com.serasa.weighing.dto.response.TruckResponse;
import com.serasa.weighing.entity.Truck;
import com.serasa.weighing.exception.ResourceNotFoundException;
import com.serasa.weighing.repository.TruckRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TruckService {

    private final TruckRepository repository;
    private final BranchService branchService;

    public List<TruckResponse> findAll() {
        return repository.findAll().stream().map(this::toResponse).toList();
    }

    public TruckResponse findById(UUID id) {
        return toResponse(findEntityById(id));
    }

    public TruckResponse create(TruckRequest request) {
        var truck = Truck.builder()
                .plate(request.getPlate())
                .tare(request.getTare())
                .build();
        if (request.getBranchId() != null) {
            truck.setBranch(branchService.findEntityById(request.getBranchId()));
        }
        return toResponse(repository.save(truck));
    }

    public TruckResponse update(UUID id, TruckRequest request) {
        var truck = findEntityById(id);
        truck.setPlate(request.getPlate());
        truck.setTare(request.getTare());
        if (request.getBranchId() != null) {
            truck.setBranch(branchService.findEntityById(request.getBranchId()));
        }
        return toResponse(repository.save(truck));
    }

    public void delete(UUID id) {
        findEntityById(id);
        repository.deleteById(id);
    }

    public Truck findEntityById(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Caminhão não encontrado: " + id));
    }

    public Truck findByPlate(String plate) {
        return repository.findByPlate(plate)
                .orElseThrow(() -> new ResourceNotFoundException("Caminhão não encontrado com placa: " + plate));
    }

    private TruckResponse toResponse(Truck truck) {
        return TruckResponse.builder()
                .id(truck.getId())
                .plate(truck.getPlate())
                .tare(truck.getTare())
                .branchId(truck.getBranch() != null ? truck.getBranch().getId() : null)
                .branchName(truck.getBranch() != null ? truck.getBranch().getName() : null)
                .build();
    }
}
