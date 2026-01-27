import { BrowserRouter, Routes, Route } from 'react-router-dom';
import { AuthProvider } from './contexts/authContext';
import MainLayout from './components/Layout/MainLayout';
import ProtectedRoute from './components/ProtectedRoute';

// Importacion de paginas
import LoginPage from './pages/auth/LoginPage';
import SignupPage from './pages/auth/SignupPage';
import TournamentsPage from './pages/participant/TournamentsPage';
import TournamentDetailPage from './pages/participant/TournamentDetailPage';
import CompetitionDetailPage from './pages/participant/CompetitionDetailPage';
import RegistrationsPage from './pages/participant/RegistrationsPage';

function App() {
  return (
    <AuthProvider>
      <BrowserRouter>
        <MainLayout>
          <Routes>
            {/* Rutas publicas */}
            <Route path="/login" element={<LoginPage />} />
            <Route path="/signup" element={<SignupPage />} />

            {/* Rutas protegidas para participantes */}
            <Route path="/" element={
              <ProtectedRoute role="participant">
                <TournamentsPage />
              </ProtectedRoute>
            } />
            <Route path="/torneos/:id" element={
              <ProtectedRoute role="participant">
                <TournamentDetailPage />
              </ProtectedRoute>
            } />
            <Route path="/torneos/:tournamentId/competencia/:competitionId" element={
              <ProtectedRoute role="participant">
                <CompetitionDetailPage />
              </ProtectedRoute>
            } />
            <Route path="/mis-inscripciones" element={
              <ProtectedRoute role="participant">
                <RegistrationsPage />
              </ProtectedRoute>
            } />
          </Routes>
        </MainLayout>
      </BrowserRouter>
    </AuthProvider>
  );
}

export default App;