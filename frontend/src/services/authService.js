const API_URL = '/auth';

export const loginUser = async (username, password) => {
    try {
        const response = await fetch(`${API_URL}/login`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({ username, password }),
        });

        if (!response.ok) {
            if (response.status === 401) throw new Error('Credenciales inválidas');
            throw new Error('Error en el servicio de login');
        }

        // Backend returns AuthResponseDTO: { token: "...", user: { id, username, email, role, ... } }
        return await response.json();
    } catch (error) {
        throw error;
    }
};

export const registerUser = async (userData) => {
    // userData matches RegisterRequestDTO: { username, email, password, phone, address, role }
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
                 if (msg.toLowerCase().includes('phone') || msg.toLowerCase().includes('telefono')) throw { phone: 'Este telefono ya está en uso' };
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
