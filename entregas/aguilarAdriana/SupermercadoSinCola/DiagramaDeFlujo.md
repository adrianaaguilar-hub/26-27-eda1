```mermaid
flowchart TD
    Start([Inicio de simulación]) --> Init[Inicializar CentroComercial\nCajas, Tiempo, etc.]
    Init --> Bucle
    
    Bucle{¿Ha finalizado\nel tiempo?}
    
    Bucle -- "No (!haFinalizado)" --> T[tiempo.avanzar()]
    
    T --> Llegada[procesarLlegadaCliente]
    Llegada --> |"Genera Cliente (Probabilidad 40%)"| L1{¿Tiene prioridad?}
    L1 -- Sí --> L2[Se salta a los no prioritarios\nen la lista enlazada]
    L1 -- No --> L3[Se forma al final de la fila]
    L2 --> Reg[registrarEstadoVirtual]
    L3 --> Reg
    Llegada --> |No llega nadie| Reg
    
    Reg --> Asignar[asignarClientesACajas]
    Asignar --> |"Busca cajas libres"| Valida{¿Caja puede\natenderlo?}
    Valida -- Sí --> A1[Pasa el Cliente a la Caja\nAvanza 'primerCliente']
    Valida -- "No (ej: >10 items en CajaExpress)" --> A2[Espera a otra caja]
    
    A1 --> Atencion[procesarAtencionCajas]
    A2 --> Atencion
    
    Atencion --> |Resta 1 ítem al cliente en caja| Mostrar[mostrarEstado]
    Mostrar --> Pausa[pausar 1 seg]
    Pausa --> Bucle
    
    Bucle -- "Sí (Cierre)" --> Fin[mostrarResumen]
    Fin --> Terminar([Fin de la ejecución])
    
    classDef highlight fill:#f9f,stroke:#333,stroke-width:2px;
    class Bucle highlight;
```

Cada vuelta representa un minuto de la simulacion. Las cajas express solo aceptan clientes con diez articulos o menos. 