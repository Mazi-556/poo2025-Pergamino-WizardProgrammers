# Frontend - Sistema de Torneos UNNOBA

Frontend React + TypeScript para la plataforma de gestión de torneos académicos.

## 🚀 Características

### Landing Page
- **Diseño institucional elegante** con gradientes y animaciones suaves
- Información sobre características principales
- Secciones dedicadas para administradores y participantes
- Call-to-action claros

### Autenticación
- Sistema de login/signup para participantes
- Autenticación de administradores
- JWT token-based con almacenamiento en localStorage
- Recuperación de sesión automática

### Panel de Administrador
- **Dashboard** con estadísticas de torneos
- **Gestión de Torneos**: crear, editar, eliminar, publicar
- **Gestión de Competencias**: crear, editar, eliminar con control de cuotas
- **Visualización de Inscripciones**: lista detallada de participantes por competencia
- **Estadísticas en Tiempo Real**: total de inscripciones y ingresos

### Área de Participantes
- **Explorar Torneos**: ver todos los torneos publicados disponibles
- **Detalle de Torneos**: ver competencias con disponibilidad de cupos
- **Sistema de Inscripciones**: registro en competencias con descuentos automáticos
- **Mis Inscripciones**: visualizar todos los registros realizados
- **Detalle de Inscripción**: información completa de cada registro

## 🛠️ Stack Tecnológico

- **React 18** - Framework UI
- **TypeScript** - Tipado estático
- **Vite** - Build tool
- **Tailwind CSS** - Estilos
- **React Router** - Navegación
- **Context API + useReducer** - Gestión de estado global

## 📦 Instalación

```bash
# Instalar dependencias
npm install

# Desarrollo
npm run dev

# Build producción
npm run build

# Preview
npm run preview
```

## 🔗 Configuración de API

El frontend se conecta a `http://localhost:8080` por defecto.

Actualiza la URL en `src/services/api.ts` si es necesario:

```typescript
const API_BASE_URL = 'http://localhost:8080';
```

## 📁 Estructura de Carpetas

```
src/
├── components/       # Componentes reutilizables
│   ├── Common/       # Botones, inputs, cards, alerts
│   ├── Layout/       # Header, Footer
│   └── ProtectedRoute.tsx
├── contexts/         # Context API (Autenticación)
├── hooks/            # Custom hooks (useAuth)
├── pages/            # Páginas principales
│   ├── admin/        # Dashboard, torneos, competencias
│   ├── auth/         # Login, signup
│   └── participant/  # Torneos, inscripciones
├── services/         # API service
├── types/            # TypeScript types
├── utils/            # Utilidades
├── App.tsx           # Configuración de rutas
├── main.tsx          # Entry point
└── index.css         # Estilos globales
```

## 🎨 Diseño

- **Paleta de colores**: Azul (primary) y Púrpura (accent)
- **Tipografía**: Inter
- **Responsive**: Mobile-first, optimizado para todos los tamaños
- **Componentes reutilizables** con variantes (size, variant)

## 🔐 Flujo de Autenticación

1. Usuario inicia sesión o se registra
2. Backend genera JWT token
3. Token se almacena en localStorage
4. Se incluye en header `Authorization: Bearer <token>` en todas las peticiones
5. Al recargar, se restaura automáticamente la sesión

## 🚦 Rutas Protegidas

- Las rutas de admin requieren `userRole === 'admin'`
- Las rutas de participante requieren `userRole === 'participant'`
- Si no estás autenticado, se redirige a `/login`

## 📱 Responsividad

Todas las páginas son totalmente responsivas:
- Mobile (< 640px)
- Tablet (640px - 1024px)  
- Desktop (> 1024px)

## 🚀 Deployment

El build genera archivos optimizados en la carpeta `dist/`:

```bash
npm run build
# Sirve los archivos en dist/ con un servidor web
```

## 📝 Notas Importantes

- El descuento del 50% en inscripciones se aplica automáticamente
- Los torneos deben estar publicados para ser visibles a participantes
- Solo se pueden inscribir si la competencia tiene cupos disponibles
- Las fechas se muestran según la zona horaria del navegador

## 🔧 Desarrollo

Para desarrollo local:

```bash
# Terminal 1: Backend Spring Boot
cd ../  # ir al directorio del backend
./mvnw spring-boot:run

# Terminal 2: Frontend
npm run dev
```

Abre http://localhost:5173 en tu navegador.

---

Desarrollado con ❤️ por Wizard Programmers - UNNOBA
