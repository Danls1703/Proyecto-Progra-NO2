-- 1. Tabla Atletas (Guarda la información principal del atleta y su deporte)
CREATE TABLE Atletas (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    apellido VARCHAR(100) NOT NULL,
    pais VARCHAR(100),
    edad INT,
    nombre_deporte VARCHAR(100),
    especialidad_deporte VARCHAR(100),
    tipo_deporte VARCHAR(100),
    mejor_marca_personal DOUBLE DEFAULT 0.0
);

-- 2. Tabla Sesiones (Guarda la información de cada entrenamiento)
CREATE TABLE Sesiones (
    id INT AUTO_INCREMENT PRIMARY KEY,
    atleta_id INT NOT NULL,
    especialidad VARCHAR(100),
    tipo VARCHAR(100),
    fecha DATETIME DEFAULT CURRENT_TIMESTAMP,
    es_extranjero BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (atleta_id) REFERENCES Atletas(id) ON DELETE CASCADE
);

-- 3. Tabla Marcas (Guarda los valores registrados en cada sesión)
CREATE TABLE Marcas (
    id INT AUTO_INCREMENT PRIMARY KEY,
    sesion_id INT NOT NULL,
    valor DOUBLE NOT NULL,
    unidad VARCHAR(50),
    fecha_registro DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (sesion_id) REFERENCES Sesiones(id) ON DELETE CASCADE
);