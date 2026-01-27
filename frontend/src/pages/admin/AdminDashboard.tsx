import { useNavigate } from 'react-router-dom';
import Button from '../../components/Common/Button';

const AdminDashboard = () => {
  const navigate = useNavigate();

  return (
    <div className="max-w-4xl mx-auto space-y-8">
      <div className="text-center">
        <h1 className="text-3xl font-bold text-gray-800">Panel de Administración</h1>
        <p className="text-gray-600 mt-2">Gestioná los torneos, competencias e inscriptos del sistema.</p>
      </div>

      <div className="grid gap-6 md:grid-cols-2">
        {/* Card de Gestion de Torneos */}
        <div className="bg-white p-6 rounded-lg shadow-md border-t-4 border-blue-600">
          <h2 className="text-xl font-bold mb-2">Torneos</h2>
          <p className="text-gray-600 mb-4">
            Creá nuevos torneos, editá los existentes o publicalos para que los vean los participantes.
          </p>
          <Button onClick={() => navigate('/admin/torneos')} className="w-full">
            Gestionar Torneos
          </Button>
        </div>

        {/* Card de Reportes o Inscriptos */}
        <div className="bg-white p-6 rounded-lg shadow-md border-t-4 border-green-600">
          <h2 className="text-xl font-bold mb-2">Inscriptos</h2>
          <p className="text-gray-600 mb-4">
            Revisá quiénes se anotaron en cada competencia y el estado de los cupos.
          </p>
          <Button variant="secondary" onClick={() => navigate('/admin/competencias')} className="w-full">
            Ver Competencias
          </Button>
        </div>
      </div>
    </div>
  );
};

export default AdminDashboard;