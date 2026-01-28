import React, { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import { useAuth } from '../../hooks/useAuth';
import { Input } from '../../components/Common/Input';
import { Button } from '../../components/Common/Button';
import { Alert } from '../../components/Common/Alert';
import { Card, CardBody } from '../../components/Common/Card';
import type { CreateParticipantRequest, ApiError } from '../../types';

export const SignupPage: React.FC = () => {
  const navigate = useNavigate();
  const { signup } = useAuth();
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const [success, setSuccess] = useState(false);

  const [formData, setFormData] = useState<CreateParticipantRequest>({
    firstName: '',
    lastName: '',
    email: '',
    dni: '',
    password: '',
  });

  const [confirmPassword, setConfirmPassword] = useState('');
  const [passwordError, setPasswordError] = useState('');

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target;
    setFormData((prev) => ({ ...prev, [name]: value }));
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setError(null);
    setPasswordError('');
    setSuccess(false);

    // Validate passwords match
    if (formData.password !== confirmPassword) {
      setPasswordError('Las contraseñas no coinciden');
      return;
    }

    // Validate password length
    if (formData.password.length < 6) {
      setPasswordError('La contraseña debe tener al menos 6 caracteres');
      return;
    }

    setLoading(true);

    try {
      await signup(formData);
      setSuccess(true);
      setTimeout(() => {
        navigate('/tournaments', { replace: true });
      }, 500);
    } catch (err) {
      const apiError = err as ApiError;
      setError(apiError.message || 'Error al crear la cuenta');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="min-h-screen flex items-center justify-center py-12 px-4 sm:px-6 lg:px-8">
      <div className="w-full max-w-md">
        {/* Logo */}
        <div className="text-center mb-8">
          <div className="flex justify-center mb-4">
            <div className="w-16 h-16 bg-gradient-to-br from-primary-600 to-accent-600 rounded-2xl flex items-center justify-center">
              <span className="text-white font-bold text-3xl">T</span>
            </div>
          </div>
          <h1 className="text-2xl font-bold text-gray-900">Torneos UNNOBA</h1>
          <p className="text-gray-600 mt-2">Crea tu cuenta para participar</p>
        </div>

        {error && (
          <Alert
            type="error"
            message={error}
            onClose={() => setError(null)}
            className="mb-6"
          />
        )}

        {success && (
          <Alert
            type="success"
            message="¡Cuenta creada! Redirigiendo..."
            className="mb-6"
          />
        )}

        <Card>
          <CardBody>
            <form onSubmit={handleSubmit} className="space-y-4">
              <div className="grid grid-cols-2 gap-4">
                <Input
                  label="Nombre"
                  placeholder="Juan"
                  name="firstName"
                  value={formData.firstName}
                  onChange={handleChange}
                  required
                />

                <Input
                  label="Apellido"
                  placeholder="Pérez"
                  name="lastName"
                  value={formData.lastName}
                  onChange={handleChange}
                  required
                />
              </div>

              <Input
                label="Email"
                type="email"
                placeholder="tu@email.com"
                name="email"
                value={formData.email}
                onChange={handleChange}
                required
                fullWidth
              />

              <Input
                label="DNI"
                placeholder="12345678"
                name="dni"
                value={formData.dni}
                onChange={handleChange}
                required
                fullWidth
              />

              <Input
                label="Contraseña"
                type="password"
                placeholder="••••••••"
                name="password"
                value={formData.password}
                onChange={handleChange}
                required
                fullWidth
              />

              <div>
                <Input
                  label="Confirmar Contraseña"
                  type="password"
                  placeholder="••••••••"
                  value={confirmPassword}
                  onChange={(e) => setConfirmPassword(e.target.value)}
                  error={passwordError}
                  required
                  fullWidth
                />
              </div>

              <Button
                type="submit"
                loading={loading}
                fullWidth
                size="lg"
                className="mt-6"
              >
                Crear Cuenta
              </Button>
            </form>
          </CardBody>
        </Card>

        <div className="mt-6 text-center">
          <p className="text-gray-600">
            ¿Ya tienes cuenta?{' '}
            <Link to="/login" className="font-semibold text-primary-600 hover:text-primary-700">
              Inicia sesión aquí
            </Link>
          </p>
        </div>
      </div>
    </div>
  );
};
