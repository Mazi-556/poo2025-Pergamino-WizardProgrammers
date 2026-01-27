import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import api from '../../services/api';
import Button from '../../components/Common/Button';
import Input from '../../components/Common/Input';

const AdminTournamentCreate = () => {
  const [formData, setFormData] = useState({
    name: '',
    description: '',
    startDate: '',
    endDate: ''
  });
  const [loading, setLoading] = useState(false);
  const navigate = useNavigate();

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setLoading(true);
    try {
      // mandamos los datos al endpoint de creacion del backend
      await api.post('/admin/tournaments', formData);
      navigate('/admin/torneos');
    } catch (err) {
      console.error('error al crear torneo', err);
      alert('no se pudo crear el torneo, revisa los datos');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="max-w-2xl mx-auto bg-white p-8 rounded shadow-md">
      <h1 className="text-2xl font-bold mb-6">Crear Nuevo Torneo</h1>
      
      <form onSubmit={handleSubmit} className="space-y-4">
        <Input 
          label="Nombre del Torneo"
          value={formData.name}
          onChange={(e) => setFormData({...formData, name: e.target.value})}
          required
        />
        
        <div className="flex flex-col space-y-1">
          <label className="text-sm font-semibold text-gray-700">Descripción</label>
          <textarea 
            className="border border-gray-300 p-2 rounded focus:ring-2 focus:ring-blue-500"
            rows={4}
            value={formData.description}
            onChange={(e) => setFormData({...formData, description: e.target.value})}
            required
          />
        </div>

        <div className="grid grid-cols-2 gap-4">
          <Input 
            label="Fecha de Inicio"
            type="date"
            value={formData.startDate}
            onChange={(e) => setFormData({...formData, startDate: e.target.value})}
            required
          />
          <Input 
            label="Fecha de Fin"
            type="date"
            value={formData.endDate}
            onChange={(e) => setFormData({...formData, endDate: e.target.value})}
            required
          />
        </div>

        <div className="pt-4 flex gap-4">
          <Button type="submit" disabled={loading}>
            {loading ? 'Guardando...' : 'Crear Torneo'}
          </Button>
          <Button variant="secondary" type="button" onClick={() => navigate(-1)}>
            Cancelar
          </Button>
        </div>
      </form>
    </div>
  );
};

export default AdminTournamentCreate;