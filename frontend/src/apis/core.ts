import axios from 'axios';

const DOMAIN = import.meta.env.VITE_API_DOMAIN;

export const axiosInstance = axios.create({
  baseURL: DOMAIN,
  timeout: 1000,
  headers: { 'Content-Type': 'application/json' },
});
