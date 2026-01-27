import React, { useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import api from '../../services/api';
import Button from '../../components/Common/Button';
import Input from '../../components/Common/Input';

const AdminCompetitionCreate = () => {
  const { id } = useParams<{ id: string }>(); // ID del torneo
  const navigate = useNavigate();
  const [loading, setLoading] = useState(false);
  const [formData, setFormData] = useState({
    name: '',
    quota: '',
    basePrice: ''
  });

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setLoading(true);
    try {
      // le pegamos al endpoint de creacion de competencias para ese torneo
      await api.post(`/admin/tournaments/${id}/competitions`, {
        ...formData,
        quota: parseInt(formData.quota),
        basePrice: parseFloat(formData.basePrice)
      });
      navigate(`/admin/torneos/${id}`);
    } catch (err) {
      console.error('error al crear competencia', err);
      alert('no se pudo crear la competencia');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="max-w-xl mx-auto bg-white p-8 rounded shadow">
      <h1 className="text-2xl font-bold mb-6">Nueva Competencia</h1>
      <form onSubmit={handleSubmit} className="space-y-4">
        <Input 
          label="Nombre de la Competencia"
          value={formData.name}
          onChange={(e) => setFormData({...formData, name: e.target.value})}
          required
        />
        <Input 
          label="Cupo Máximo"
          type="number"
          value={formData.quota}
          onChange={(e) => setFormData({...formData, quota: e.target.value})}
          required
        />
        <Input 
          label="Precio Base"
          type="number"
          step="0.01"
          value={formData.basePrice}
          onChange={(e) => setFormData({...formData, basePrice: e.target.value})}
          required
        />
        <div className="pt-4 flex gap-4">
          <Button type="submit" disabled={loading}>
            {loading ? 'Guardando...' : 'Crear Competencia'}
          </Button>
          <Button variant="secondary" type="button" onClick={() => navigate(-1)}>
            Cancelar
          </Button>
        </div>
      </form>
    </div>
  );
};

export default AdminCompetitionCreate;