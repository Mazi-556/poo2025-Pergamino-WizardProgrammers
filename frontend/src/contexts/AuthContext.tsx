import React, { createContext, useReducer, useCallback, useEffect, useState } from 'react';
import type { AuthContextType, UserRole, Admin, Participant, CreateParticipantRequest } from '../types';
import { apiService } from '../services/api';

interface AuthState {
  userRole: UserRole;
  user: Admin | Participant | null;
  token: string | null;
  isAuthenticated: boolean;
}

type AuthAction =
  | { type: 'LOGIN_SUCCESS'; payload: { token: string; user: Admin | Participant; role: UserRole } }
  | { type: 'LOGOUT' }
  | { type: 'RESTORE_TOKEN'; payload: { token: string; user: Admin | Participant; role: UserRole } }
  | { type: 'ERROR' };

const initialState: AuthState = {
  userRole: null,
  user: null,
  token: null,
  isAuthenticated: false,
};

function authReducer(state: AuthState, action: AuthAction): AuthState {
  switch (action.type) {
    case 'LOGIN_SUCCESS':
      return {
        ...state,
        token: action.payload.token,
        user: action.payload.user,
        userRole: action.payload.role,
        isAuthenticated: true,
      };
    case 'LOGOUT':
      return initialState;
    case 'RESTORE_TOKEN':
      return {
        ...state,
        token: action.payload.token,
        user: action.payload.user,
        userRole: action.payload.role,
        isAuthenticated: true,
      };
    case 'ERROR':
      return state;
    default:
      return state;
  }
}

export const AuthContext = createContext<AuthContextType | undefined>(undefined);

export const AuthProvider: React.FC<{ children: React.ReactNode }> = ({ children }) => {
  const [state, dispatch] = useReducer(authReducer, initialState);
  const [isInitializing, setIsInitializing] = useState(true);

  // Restore token from localStorage on mount
  useEffect(() => {
    const savedToken = localStorage.getItem('token');
    const savedUser = localStorage.getItem('user');
    const savedRole = localStorage.getItem('userRole') as UserRole;

    if (savedToken && savedUser && savedRole) {
      try {
        const user = JSON.parse(savedUser);
        dispatch({
          type: 'RESTORE_TOKEN',
          payload: { token: savedToken, user, role: savedRole },
        });
        apiService.setToken(savedToken);
      } catch (e) {
        localStorage.removeItem('token');
        localStorage.removeItem('user');
        localStorage.removeItem('userRole');
      }
    }
    setIsInitializing(false);
  }, []);

  const login = useCallback(async (email: string, password: string) => {
    try {
      let response;
      let role: UserRole = null;

      // Try admin login first
      try {
        response = await apiService.adminLogin(email, password);
        role = 'admin';
      } catch {
        // If admin login fails, try participant login
        response = await apiService.participantLogin(email, password);
        role = 'participant';
      }

      const { token: rawToken, user } = response as { token: string; user: Admin | Participant };
      // Remove "Bearer " prefix if it exists
      const token = rawToken.replace(/^Bearer\s+/i, '');
      apiService.setToken(token);

      localStorage.setItem('token', token);
      localStorage.setItem('user', JSON.stringify(user));
      localStorage.setItem('userRole', role);

      dispatch({
        type: 'LOGIN_SUCCESS',
        payload: { token, user, role },
      });
    } catch (error) {
      console.error('Login error:', error);
      dispatch({ type: 'ERROR' });
      throw error;
    }
  }, []);

  const signup = useCallback(async (data: CreateParticipantRequest) => {
    try {
      await apiService.participantSignup(data);
      // Auto-login after signup
      await login(data.email, data.password);
    } catch (error) {
      console.error('Signup error:', error);
      dispatch({ type: 'ERROR' });
      throw error;
    }
  }, [login]);

  const logout = useCallback(() => {
    localStorage.removeItem('token');
    localStorage.removeItem('user');
    localStorage.removeItem('userRole');
    apiService.setToken(null);
    dispatch({ type: 'LOGOUT' });
  }, []);

  const value: AuthContextType = {
    userRole: state.userRole,
    user: state.user,
    token: state.token,
    login,
    signup,
    logout,
    isAuthenticated: state.isAuthenticated,
    isInitializing,
  };

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
};
