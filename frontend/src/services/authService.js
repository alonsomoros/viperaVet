const API_URL = '/auth';

/*
{
    "email": "alon@gmail.com",
    "password": "password"
} 
*/
export const loginUser = async (email, password, role) => {
    try {
        const endpoint = role === 'user' ? `${API_URL}/login/user` : `${API_URL}/login/vet`;
        
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
/**
{
    "name": "Alon",
    "surnames": "Owner",
    "email": "alon@gmail.com",
    "phone": "625375613",
    "dni": "54191902R",
    "role": "USER"
}
*/
export const registerUser = async (userData, token) => {
    try {
        const headers = {
            'Content-Type': 'application/json',
        };
        if (token) headers['Authorization'] = `Bearer ${token}`;

        const response = await fetch(`${API_URL}/register/user`, {
            method: 'POST',
            headers: headers,
            body: JSON.stringify(userData),
        });

        if (!response.ok) {
            const errorData = await response.json().catch(() => ({}));

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

export const verifyToken = async (token) => {
    try {
        const response = await fetch(`${API_URL}/verify?token=${token}`, {
            method: 'GET',
        });

        if (!response.ok) {
            const errorData = await response.json().catch(() => ({}));
            throw new Error(errorData.message || 'Token no válido o expirado');
        }
    } catch (error) {
        throw error;
    }
};

export const activateAccount = async (activationData) => {
    try {
        const response = await fetch(`${API_URL}/activate`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(activationData),
        });

        if (!response.ok) {
            const errorData = await response.json().catch(() => ({}));
            throw new Error(errorData.message || 'Error al activar la cuenta');
        }

        return await response.json();
    } catch (error) {
        throw error;
    }
};
