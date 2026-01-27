import axios from 'axios';

// creamos la instancia de axios para no repetir la url
const api = axios.create({
  baseURL: 'http://localhost:8080', // la url del backend de spring
});

// este interceptor sirve para meter el token en los headers si existe
// asi no tenemos que hacerlo a mano en cada peticion
api.interceptors.request.use((config) => {
  const token = localStorage.getItem('token');
  if (token) {
    // aca usamos el formato Bearer que espera tu JwtTokenUtil.java
    config.headers.Authorization = token.startsWith('Bearer ') ? token : `Bearer ${token}`;
  }
  return config;
});

export default api;