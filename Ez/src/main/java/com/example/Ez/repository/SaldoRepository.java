package com.example.Ez.repository;

import com.example.Ez.model.Saldo;
import com.example.Ez.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SaldoRepository extends JpaRepository<Saldo, Long> {
    Optional<Saldo> findByUsuario(Usuario usuario);

    Optional<Saldo> findByUsuarioId(Long usuarioId);
}
