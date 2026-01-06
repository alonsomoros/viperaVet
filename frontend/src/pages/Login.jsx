import React, { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import { loginUser } from '../services/authService';
import './Login.css';

const Login = () => {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState('');
    const { login } = useAuth();
    const navigate = useNavigate();

    const handleSubmit = async (e) => {
        e.preventDefault();
        setError('');
        try {
            const data = await loginUser(username, password);
            login(data);
            navigate('/home');
        } catch (err) {
            // For login, usually it's a generic "Invalid credentials", so highlight both or show global.
            // But to satisfy "inputs in red", we can check text or just set error.
            setError(err.message || 'Error al iniciar sesión');
        }
    };

    return (
        <div className="login-container">
            <form className="login-form" onSubmit={handleSubmit}>
                <h2>Bienvenido</h2>
                {error && <p className="error-message">{error}</p>}
                
                <div className="form-group">
                    <label>Usuario</label>
                    <input 
                        type="text" 
                        className={error ? 'input-error' : ''}
                        value={username}
                        onChange={(e) => setUsername(e.target.value)}
                        required 
                        placeholder="Ingresa tu usuario"
                    />
                </div>

                <div className="form-group">
                    <label>Contraseña</label>
                    <input 
                        type="password" 
                        className={error ? 'input-error' : ''}
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                        required 
                        placeholder="••••••••"
                    />
                </div>

                <button type="submit">Entrar</button>
                <p className="register-link">
                    ¿No tienes cuenta? <Link to="/register">Regístrate aquí</Link>
                </p>
            </form>
        </div>
    );
};

export default Login;
