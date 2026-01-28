import React, { useEffect, useState } from 'react';
import { apiService } from '../../services/api'; 

interface Registration {
  id: number;
  date: string;  // Viene como 'date' del backend
  price: number; // Viene como 'price' del backend
  tournamentName: string;
  competitionName: string;
  quota: number; // El campo nuevo que agregamos
}

const RegistrationsPage: React.FC = () => {
  const [registrations, setRegistrations] = useState<Registration[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    apiService.getMyRegistrations()
      .then((res: any) => {
        if (Array.isArray(res)) {
          setRegistrations(res);
        } else {
          setRegistrations([]);
        }
      })
      .catch((err: any) => console.error('error:', err))
      .finally(() => setLoading(false));
  }, []);

  const formatDate = (dateStr: string) => {
    if (!dateStr) return '---';
    // Manejo de la fecha por si viene como array [año, mes, día] o string
    const date = new Date(dateStr);
    return isNaN(date.getTime()) ? dateStr : date.toLocaleDateString('es-AR');
  };

  const total = registrations.reduce((acc, reg) => acc + (Number(reg.price) || 0), 0);

  if (loading) return <div className="p-10 text-center text-gray-500">cargando inscripciones...</div>;

  return (
    <div className="max-w-4xl mx-auto px-4 py-8">
      <div className="flex justify-between items-center mb-8 bg-blue-700 text-white p-6 rounded-xl shadow-lg">
        <div>
          <h1 className="text-2xl font-bold">Mis Inscripciones</h1>
          <p className="text-blue-100 text-sm italic">panel de control unnoba</p>
        </div>
        <div className="text-right">
          <p className="text-xs uppercase opacity-70 font-semibold">total a pagar</p>
          <p className="text-3xl font-black">${total.toLocaleString('es-AR', { minimumFractionDigits: 2 })}</p>
        </div>
      </div>

      <div className="space-y-6">
        {registrations.length === 0 ? (
          <div className="bg-white p-12 text-center rounded-xl shadow border border-gray-100">
            <p className="text-gray-400">no tenes ninguna inscripcion registrada todavía.</p>
          </div>
        ) : (
          registrations.map((reg) => (
            <div key={reg.id} className="bg-white p-6 rounded-xl shadow-md border-l-8 border-green-500 hover:scale-[1.01] transition-transform">
              <div className="flex justify-between items-start">
                <div>
                  <p className="text-xs font-bold text-blue-600 uppercase mb-1">{reg.tournamentName}</p>
                  <h3 className="font-black text-xl text-gray-800">{reg.competitionName}</h3>
                  <div className="mt-3 flex gap-4 text-sm text-gray-500">
                    <span className="bg-gray-100 px-2 py-1 rounded">Fecha: <b>{formatDate(reg.date)}</b></span>
                    <span className="bg-blue-50 text-blue-700 px-2 py-1 rounded font-bold">Cupo total: {reg.quota}</span>
                  </div>
                </div>
                <div className="text-right">
                  <p className="text-2xl font-black text-gray-900">${Number(reg.price).toLocaleString('es-AR', { minimumFractionDigits: 2 })}</p>
                  <span className="text-[10px] font-bold text-green-600 bg-green-50 px-2 py-1 rounded-full uppercase">activa</span>
                </div>
              </div>
            </div>
          ))
        )}
      </div>
    </div>
  );
};

export default RegistrationsPage;