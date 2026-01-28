// Auth & Admin
export interface AdminLogin {
  email: string;
  password: string;
}

export interface AdminAccount {
  email: string;
  password: string;
}

export interface Admin {
  id: number;
  email: string;
}

export interface AuthResponse {
  token: string;
  user: Admin | Participant;
}

// Tournament
export interface Tournament {
  id: number;
  name: string;
  description: string;
  startDate: string;
  endDate: string;
  published: boolean;
  adminId: number;
}

export interface AdminTournamentDetail {
  id: number;
  name: string;
  description: string;
  startDate: string;
  endDate: string;
  published: boolean;
  totalRegistrations: number;
  totalRaised: number;
  competitions: CompetitionSummary[];
}

export interface AdminTournamentSummary {
  id: number;
  name: string;
  description: string;
  startDate: string;
  endDate: string;
  published: boolean;
}

export interface TournamentResponse {
  id: number;
  name: string;
  description: string;
  startDate: string;
  endDate: string;
  published: boolean;
}

export interface TournamentCreateUpdate {
  name: string;
  description: string;
  startDate: string;
  endDate: string;
}

// Competition
export interface CompetitionSummary {
  id: number;
  name: string;
  quota: number;
  basePrice: number;
  registrations: number;
}

export interface AdminCompetitionDetail {
  id: number;
  name: string;
  quota: number;
  basePrice: number;
  totalRegistrations: number;
  totalRaised: number;
}

export interface AdminCompetitionCreateUpdate {
  name: string;
  quota: number;
  basePrice: number;
}

// Participant
export interface CreateParticipantRequest {
  firstName: string;
  lastName: string;
  email: string;
  dni: string;
  password: string;
}

export interface Participant {
  id: number;
  firstName: string;
  lastName: string;
  email: string;
  dni: string;
}

export interface ParticipantLogin {
  email: string;
  password: string;
}

// Registration
export interface RegistrationResponse {
  id: number;
  participantId: number;
  competitionId: number;
  competitionName: string;
  tournamentId: number;
  tournamentName: string;
  registrationDate: string;
  finalPrice: number;
}

export interface AdminCompetitionRegistrationDTO {
  id: number;
  participantFirstName: string;
  participantLastName: string;
  participantEmail: string;
  participantDni: string;
  registrationDate: string;
  finalPrice: number;
}

export interface ParticipantRegistrationDTO {
  competitionId: number;
  participantId: number;
  registrationDate: string;
  finalPrice: number;
}

export interface ParticipantRegistrationDetail {
  competitionId: number;
  competitionName: string;
  tournamentId: number;
  tournamentName: string;
  registrationDate: string;
  finalPrice: number;
}

// Auth Context
export type UserRole = 'admin' | 'participant' | null;

export interface AuthContextType {
  userRole: UserRole;
  user: Admin | Participant | null;
  token: string | null;
  login: (email: string, password: string) => Promise<void>;
  signup: (data: CreateParticipantRequest) => Promise<void>;
  logout: () => void;
  isAuthenticated: boolean;
  isInitializing: boolean;
}

export interface ApiError {
  message: string;
  status?: number;
}
