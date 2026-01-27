import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import api from '../../services/api';

const SignupPage = () => {
  const [formData, setFormData] = useState({
    name: '',
    surname: '',
    dni: '',
    email: '',
    password: ''
  });
  const [error, setError] = useState('');
  const navigate = useNavigate();

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      // usamos el endpoint del backend para crear participantes
      await api.post('/participants/account', {
        ...formData,
        dni: parseInt(formData.dni) // nos aseguramos que sea numero
      });
      navigate('/login'); // si sale bien, a loguearse
    } catch (err: any) {
      setError(err.response?.data?.error || 'error al crear la cuenta');
    }
  };

  return (
    <div className="max-w-md mx-auto mt-10 p-6 bg-white rounded shadow">
      <h2 className="text-2xl font-bold mb-4 text-center">Registrarse</h2>
      {error && <p className="text-red-500 mb-4">{error}</p>}
      <form onSubmit={handleSubmit} className="space-y-4">
        <input 
          type="text" placeholder="Nombre" className="w-full p-2 border rounded"
          onChange={(e) => setFormData({...formData, name: e.target.value})} required
        />
        <input 
          type="text" placeholder="Apellido" className="w-full p-2 border rounded"
          onChange={(e) => setFormData({...formData, surname: e.target.value})} required
        />
        <input 
          type="number" placeholder="DNI" className="w-full p-2 border rounded"
          onChange={(e) => setFormData({...formData, dni: e.target.value})} required
        />
        <input 
          type="email" placeholder="Email" className="w-full p-2 border rounded"
          onChange={(e) => setFormData({...formData, email: e.target.value})} required
        />
        <input 
          type="password" placeholder="Contraseña" className="w-full p-2 border rounded"
          onChange={(e) => setFormData({...formData, password: e.target.value})} required
        />
        <button type="submit" className="w-full bg-green-600 text-white py-2 rounded">
          Crear Cuenta
        </button>
      </form>
    </div>
  );
};

export default SignupPage;