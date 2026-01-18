const API_URL = '/auth';

export const loginUser = async (email, password, role) => {
    try {
        const endpoint = role === 'user' ? `${API_URL}/login/owner` : `${API_URL}/login/vet`;
        
        if (role !== 'user' && role !== 'vet') {
            throw new Error('Role no válido');
        }

        const response = await fetch(endpoint, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({ email, password }),
        });

        if (!response.ok) {
            if (response.status === 401) throw new Error('Credenciales inválidas');
            throw new Error('Error en el servicio de login');
        }
        return await response.json();
    } catch (error) {
        throw error;
    }
};

export const registerUser = async (userData) => {
    try {
        const response = await fetch(`${API_URL}/register`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(userData),
        });

        if (!response.ok) {
            const errorData = await response.json().catch(() => ({}));

            // Structured error throwing for frontend mapping
            if (response.status === 409) {
                const msg = errorData.message || 'El usuario o email ya existe';
                if (msg.toLowerCase().includes('email')) throw { email: 'Este email ya está en uso' };
                if (msg.toLowerCase().includes('user') || msg.toLowerCase().includes('usuario')) throw { username: 'Este usuario ya existe' };
                throw { global: msg };
            }
            if (response.status === 400) {
                const msg = errorData.message || 'Datos inválidos';
                if (msg.toLowerCase().includes('password') || msg.toLowerCase().includes('contraseña')) throw { password: 'La contraseña debe tener al menos 6 caracteres' };
                if (msg.toLowerCase().includes('email')) throw { email: 'Formato de email inválido' };
                throw { global: msg };
            }
            throw { global: errorData.message || 'Error en el registro' };
        }

        return await response.json();
    } catch (error) {
        throw error;
    }
};
