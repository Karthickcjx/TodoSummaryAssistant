import axios from 'axios';

// REACT_APP_API_BASE_URL is the backend API root (for example, http://localhost:8080/api).
// Accept an existing /todos suffix too, so older local/Docker configuration keeps working.
const API_ROOT = (process.env.REACT_APP_API_BASE_URL || 'http://localhost:8080/api').replace(/\/$/, '');
const API_BASE_URL = API_ROOT.endsWith('/todos') ? API_ROOT : `${API_ROOT}/todos`;

const todoService = {
  getAllTodos: () => axios.get(API_BASE_URL),
  createTodo: (todo) => axios.post(API_BASE_URL, todo),
  updateTodo: (id, todo) => axios.put(`${API_BASE_URL}/${id}`, todo),
  deleteTodo: (id) => axios.delete(`${API_BASE_URL}/${id}`),
  summarizeTodos: () => axios.post(`${API_BASE_URL}/summarize`),
};

export default todoService;
