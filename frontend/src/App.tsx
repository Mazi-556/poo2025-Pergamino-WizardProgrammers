import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import { AuthProvider } from './contexts/AuthContext';
import { Header } from './components/Layout/Header';
import { Footer } from './components/Layout/Footer';
import { ProtectedRoute } from './components/ProtectedRoute';

// Pages
import { HomePage } from './pages/HomePage';
import { LoginPage } from './pages/auth/LoginPage';
import { SignupPage } from './pages/auth/SignupPage';

// Admin Pages
import { AdminDashboard } from './pages/admin/AdminDashboard';
import { AdminTournaments } from './pages/admin/AdminTournaments';
import { AdminTournamentDetail } from './pages/admin/AdminTournamentDetail';
import { AdminCompetitions } from './pages/admin/AdminCompetitions';
import { AdminCompetitionDetail } from './pages/admin/AdminCompetitionDetail';

// Participant Pages
import { TournamentsPage } from './pages/participant/TournamentsPage';
import { TournamentDetailPage } from './pages/participant/TournamentDetailPage';
import { CompetitionDetailPage } from './pages/participant/CompetitionDetailPage';
import RegistrationsPage from './pages/participant/RegistrationsPage';
import { RegistrationDetailPage } from './pages/participant/RegistrationDetailPage';

function App() {
  return (
    <BrowserRouter>
      <AuthProvider>
        <div className="flex flex-col min-h-screen bg-gray-50">
          <Header />
          <main className="flex-grow">
            <Routes>
              {/* Public Routes */}
              <Route path="/" element={<HomePage />} />
              <Route path="/login" element={<LoginPage />} />
              <Route path="/signup" element={<SignupPage />} />

              {/* Admin Routes */}
              <Route
                path="/admin/dashboard"
                element={
                  <ProtectedRoute requiredRole="admin">
                    <AdminDashboard />
                  </ProtectedRoute>
                }
              />
              <Route
                path="/admin/tournaments"
                element={
                  <ProtectedRoute requiredRole="admin">
                    <AdminTournaments />
                  </ProtectedRoute>
                }
              />
              <Route
                path="/admin/tournaments/:tournamentId"
                element={
                  <ProtectedRoute requiredRole="admin">
                    <AdminTournamentDetail />
                  </ProtectedRoute>
                }
              />
              <Route
                path="/admin/tournaments/:tournamentId/competitions"
                element={
                  <ProtectedRoute requiredRole="admin">
                    <AdminCompetitions />
                  </ProtectedRoute>
                }
              />
              <Route
                path="/admin/tournaments/:tournamentId/competitions/:competitionId"
                element={
                  <ProtectedRoute requiredRole="admin">
                    <AdminCompetitionDetail />
                  </ProtectedRoute>
                }
              />

              {/* Participant Routes */}
              <Route
                path="/tournaments"
                element={
                  <ProtectedRoute requiredRole="participant">
                    <TournamentsPage />
                  </ProtectedRoute>
                }
              />
              <Route
                path="/tournaments/:tournamentId"
                element={
                  <ProtectedRoute requiredRole="participant">
                    <TournamentDetailPage />
                  </ProtectedRoute>
                }
              />
              <Route
                path="/tournaments/:tournamentId/competitions/:competitionId"
                element={
                  <ProtectedRoute requiredRole="participant">
                    <CompetitionDetailPage />
                  </ProtectedRoute>
                }
              />
              <Route
                path="/registrations"
                element={
                  <ProtectedRoute requiredRole="participant">
                    <RegistrationsPage />
                  </ProtectedRoute>
                }
              />
              <Route
                path="/registrations/:registrationId"
                element={
                  <ProtectedRoute requiredRole="participant">
                    <RegistrationDetailPage />
                  </ProtectedRoute>
                }
              />

              {/* 404 */}
              <Route path="*" element={<Navigate to="/" replace />} />
            </Routes>
          </main>
          <Footer />
        </div>
      </AuthProvider>
    </BrowserRouter>
  );
}

export default App;
