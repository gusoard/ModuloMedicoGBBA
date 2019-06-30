package gustavorivera.proyectogrado.gbba.modulomedicogbba.model


val DATABASE_VERSION: Int = 1
val DATABASE_NAME: String = "fichas.db"

// Tablas
val FICHAS_TABLE_NAME: String = "fichas"
val USUARIOS_TABLE_NAME: String = "usuarios"

// Nombres de columnas

// Tabla Usuarios
val USUARIOS_ID: String = "id"
val USUARIOS_NAME: String = "nombre"
val USUARIOS_CORREO: String = "correo"
val USUARIOS_DOC_IDENTIDAD: String = "doc_identidad"
val USUARIOS_ULT_ING: String = "ultimo_ingreso"
val USUARIOS_GRUPO_SANG: String = "grupo_sanguineo"

// Tabla Fichas
val FICHAS_ID: String = "id"
val FICHAS_ID_USUARIO: String = "idUsuario"
val FICHAS_FECHA: String = "fecha_mod"
val FICHAS_SPO2: String = "spo2"
val FICHAS_ALTURA: String = "altura"
val FICHAS_PPM: String = "ppm"
val FICHAS_MARCADA: String = "marcada"
