const api = {
  baseUrl: 'http://localhost:8080/api',

  async get(endpoint) {
    try {
      const response = await fetch(`${this.baseUrl}${endpoint}`);
      return await response.json();
    } catch (error) {
      console.error('API Error:', error);
      return [];
    }
  },

  async post(endpoint, data) {
    try {
      const response = await fetch(`${this.baseUrl}${endpoint}`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(data)
      });
      return await response.json();
    } catch (error) {
      console.error('API Error:', error);
      throw error;
    }
  },

  async put(endpoint, data) {
    try {
      const response = await fetch(`${this.baseUrl}${endpoint}`, {
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(data)
      });
      return await response.json();
    } catch (error) {
      console.error('API Error:', error);
      throw error;
    }
  },

  async delete(endpoint) {
    try {
      await fetch(`${this.baseUrl}${endpoint}`, { method: 'DELETE' });
    } catch (error) {
      console.error('API Error:', error);
      throw error;
    }
  },
   async uploadFile(endpoint, file) {
      try {
        const formData = new FormData();
        formData.append('file', file);
        const response = await fetch(`${this.baseUrl}${endpoint}`, {
          method: 'POST',
          body: formData
        });
        return await response.json();
      } catch (error) {
        console.error('API Error:', error);
        throw error;
      }
    },

  async downloadTemplate(endpoint) {
      try {
        const response = await fetch(`${this.baseUrl}${endpoint}`);
        const blob = await response.blob();
        return blob;
      } catch (error) {
        console.error('API Error:', error);
        throw error;
      }
    }
};


export default api;