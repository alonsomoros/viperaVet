
const API_URL = '/users';

export const checkEmail = async (email, token) => {
    const headers = {
        'Content-Type': 'application/json',
    };

    if (token) {
        headers['Authorization'] = `Bearer ${token}`;
    }

    const response = await fetch(`${API_URL}/check-email?email=${encodeURIComponent(email)}`, {
        method: 'GET',
        headers: headers,
    });

    if (!response.ok) {
        if (response.status === 403) throw new Error('No tienes permisos para realizar esta acción');
        if (response.status === 500) throw new Error('Error en el servidor');
        throw new Error('Error al verificar el email');
    }

    return await response.json();
};
