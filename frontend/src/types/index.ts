// define la estructura del Usuario (Admin o Participante)
export interface User {
  email: string;
  role: 'admin' | 'participant'; // Diferenciamos permisos
}

// espejamos el modelo que tenemos en Java de 'Tournament'
export interface Tournament {
  id: number;
  name: string;
  description: string;
  startDate: string;
  endDate: string;
  published: boolean;
}

// espejamos
export interface Competition {
  id: number;
  name: string;
  quota: number;
  basePrice: number;
}

// espejamos
export interface Registration {
  id: number;
  date: string;
  price: number;
  tournamentName?: string;
  competitionName?: string;
}