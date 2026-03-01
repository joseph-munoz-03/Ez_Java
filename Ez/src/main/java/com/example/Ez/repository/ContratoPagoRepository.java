package com.example.Ez.repository;

import com.example.Ez.model.ContratoPago;
import com.example.Ez.model.Contrato;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ContratoPagoRepository extends JpaRepository<ContratoPago, Long> {
    List<ContratoPago> findByContrato(Contrato contrato);
    List<ContratoPago> findByContratoOrderByNumeroPago(Contrato contrato);
    Optional<ContratoPago> findByContratoAndNumeroPago(Contrato contrato, Integer numeroPago);
    List<ContratoPago> findByEstado(ContratoPago.EstadoPago estado);
    
    @Query("SELECT cp FROM ContratoPago cp WHERE cp.contrato = :contrato AND cp.estado = 'PENDIENTE'")
    List<ContratoPago> findPagasPendientes(@Param("contrato") Contrato contrato);
    
    @Query("SELECT cp FROM ContratoPago cp WHERE cp.fechaVencimiento <= :fecha AND cp.estado = 'PENDIENTE'")
    List<ContratoPago> findPagosVencidos(@Param("fecha") LocalDate fecha);
    
    @Query("SELECT cp FROM ContratoPago cp WHERE cp.contrato IN :contratos")
    List<ContratoPago> findByContratos(@Param("contratos") List<Contrato> contratos);
}
