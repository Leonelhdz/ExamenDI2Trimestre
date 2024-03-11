package com.example.examendesarrollointerface;

import com.example.examendesarrollointerface.domain.Clientes;
import lombok.Getter;
import lombok.Setter;

public class Session {
    @Getter
    @Setter
    private static Clientes currentCliente;
}
