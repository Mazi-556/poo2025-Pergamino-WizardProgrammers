import type { ApiError } from '../types';

const API_BASE_URL = 'http://localhost:8080';

class ApiService {
  private token: string | null = null;

  setToken(token: string | null) {
    this.token = token;
  }

  private async request<T>(
    endpoint: string,
    options: RequestInit = {}
  ): Promise<T> {
    const headers: Record<string, string> = {
      'Content-Type': 'application/json',
      ...(options.headers as Record<string, string> || {}),
    };

    if (this.token) {
      headers.Authorization = `Bearer ${this.token}`;
    }

    try {
      const response = await fetch(`${API_BASE_URL}${endpoint}`, {
        ...options,
        headers,
        mode: 'cors',
        credentials: 'include',
      });

      if (!response.ok) {
        const error: ApiError = {
          message: `Error: ${response.statusText}`,
          status: response.status,
        };
        try {
          const data = await response.json();
          error.message = data.message || data.error || error.message;
        } catch (e) {
          console.error('Error parsing error response:', e);
        }
        throw error;
      }

      if (response.status === 204) {
        return {} as T;
      }

      const text = await response.text();
      if (!text) {
        return {} as T;
      }
      return JSON.parse(text);
    } catch (err: any) {
      console.error('Request error:', err);
      if (err instanceof TypeError) {
        throw {
          message: `No se puede conectar al servidor en ${API_BASE_URL}. ¿Está el backend corriendo?`,
          status: 0,
        } as ApiError;
      }
      if (err.message) {
        throw err;
      }
      throw {
        message: err?.toString() || 'Error desconocido',
        status: 0,
      } as ApiError;
    }
  }

  // ==================== AUTH ====================
  adminLogin(email: string, password: string) {
    return this.request('/admin/auth', {
      method: 'POST',
      body: JSON.stringify({ email, password }),
    });
  }

  adminRegister(email: string, password: string) {
    return this.request('/admin/register', {
      method: 'POST',
      body: JSON.stringify({ email, password }),
    });
  }

  participantLogin(email: string, password: string) {
    return this.request('/participants/auth', {
      method: 'POST',
      body: JSON.stringify({ email, password }),
    });
  }

  participantSignup(data: {
    firstName: string;
    lastName: string;
    email: string;
    dni: string;
    password: string;
  }) {
    return this.request('/participants/account', {
      method: 'POST',
      body: JSON.stringify(data),
    });
  }

  // ==================== TOURNAMENTS ====================
  // Admin - Get all tournaments
  getAdminTournaments() {
    return this.request('/admin/tournaments');
  }

  // Admin - Create tournament
  createTournament(data: {
    name: string;
    description: string;
    startDate: string;
    endDate: string;
  }) {
    return this.request('/admin/tournaments', {
      method: 'POST',
      body: JSON.stringify(data),
    });
  }

  // Admin - Get tournament detail
  getAdminTournamentDetail(tournamentId: number) {
    return this.request(`/admin/tournaments/${tournamentId}`);
  }

  // Admin - Update tournament
  updateTournament(
    tournamentId: number,
    data: {
      name: string;
      description: string;
      startDate: string;
      endDate: string;
    }
  ) {
    return this.request(`/admin/tournaments/${tournamentId}`, {
      method: 'PUT',
      body: JSON.stringify(data),
    });
  }

  // Admin - Delete tournament
  deleteTournament(tournamentId: number) {
    return this.request(`/admin/tournaments/${tournamentId}`, {
      method: 'DELETE',
    });
  }

  // Admin - Publish tournament
  publishTournament(tournamentId: number) {
    return this.request(`/admin/tournaments/${tournamentId}/published`, {
      method: 'PATCH',
    });
  }

  // Participant - Get published tournaments
  getTournaments() {
    return this.request('/tournaments');
  }

  // Participant - Get tournament competitions
  getTournamentCompetitions(tournamentId: number) {
    return this.request(`/tournaments/${tournamentId}/competitions`);
  }

  // ==================== COMPETITIONS ====================
  // Admin - Get competitions
  getAdminCompetitions(tournamentId: number) {
    return this.request(`/admin/tournaments/${tournamentId}/competitions`);
  }

  // Admin - Create competition
  createCompetition(
    tournamentId: number,
    data: {
      name: string;
      quota: number;
      basePrice: number;
    }
  ) {
    return this.request(`/admin/tournaments/${tournamentId}/competitions`, {
      method: 'POST',
      body: JSON.stringify(data),
    });
  }

  // Admin - Get competition detail
  getAdminCompetitionDetail(tournamentId: number, competitionId: number) {
    return this.request(
      `/admin/tournaments/${tournamentId}/competitions/${competitionId}`
    );
  }

  // Admin - Update competition
  updateCompetition(
    tournamentId: number,
    competitionId: number,
    data: {
      name: string;
      quota: number;
      basePrice: number;
    }
  ) {
    return this.request(
      `/admin/tournaments/${tournamentId}/competitions/${competitionId}`,
      {
        method: 'PUT',
        body: JSON.stringify(data),
      }
    );
  }

  // Admin - Delete competition
  deleteCompetition(tournamentId: number, competitionId: number) {
    return this.request(
      `/admin/tournaments/${tournamentId}/competitions/${competitionId}`,
      {
        method: 'DELETE',
      }
    );
  }

  // Admin - Get competition registrations
  getCompetitionRegistrations(tournamentId: number, competitionId: number) {
    return this.request(
      `/admin/tournaments/${tournamentId}/competitions/${competitionId}/registrations`
    );
  }

  // Participant - Get competition detail
  getCompetitionDetail(tournamentId: number, competitionId: number) {
    return this.request(
      `/tournaments/${tournamentId}/competitions/${competitionId}`
    );
  }

  // ==================== REGISTRATIONS ====================
  // Create registration
  createRegistration(tournamentId: number, competitionId: number) {
    return this.request(
      `/tournaments/${tournamentId}/competitions/${competitionId}/registrations`,
      {
        method: 'POST',
      }
    );
  }

  // Get my registrations
  getMyRegistrations() {
    return this.request('/registrations');
  }

  // Get registration detail
  getRegistrationDetail(registrationId: number) {
    return this.request(`/registrations/${registrationId}`);
  }
}
export default ApiService;
export const apiService = new ApiService();
