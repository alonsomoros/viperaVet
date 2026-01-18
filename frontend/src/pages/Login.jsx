import React, { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import { loginUser } from '../services/authService';
import { FaUser, FaHandHoldingMedical } from "react-icons/fa";
import './Login.css';

const Login = () => {
    // Para gestionar lo escrito en los inputs del formulario
    const [credentials, setCredentials] = useState({
        email: '',
        password: ''
    });
    const [loginType, setLoginType] = useState("user");
    const [error, setError] = useState('');
    
    const { login } = useAuth();
    const navigate = useNavigate();

    // Para gestionar los cambios en los inputs del formulario
    const handleChange = (e) => {
        const { name, value } = e.target;
        setCredentials(prev => ({
            ...prev,
            [name]: value
        }));
    };

    // Para gestionar el envio del formulario
    const handleSubmit = async (e) => {
        e.preventDefault();
        setError('');
        try {
            const data = await loginUser(credentials.email, credentials.password, loginType);
            login(data);
            navigate('/home');
        } catch (err) {
            setError(err.message || 'Error al iniciar sesión');
        }
    };

    // Para gestionar el tipo de usuario (usuario o veterinario)
    const config = {
        user: { label: "Correo electrónico", placeholder: "correo@gmail.com" },
        vet: { label: "Correo de empresa", placeholder: "correo@empresa.com" }
    };

    return (
        <div className="login-container">
            <form className="login-form" onSubmit={handleSubmit}>
                <h2>Bienvenido a <span style={{ color: "var(--vet-primary-green)" }}>ViperaVet</span></h2>
                {error && <p className="error-message">{error}</p>}

                <div className="button-group">
                    <button
                        type="button"
                        className={loginType === "user" ? "active" : ""}
                        onClick={() => setLoginType("user")}
                    >
                        <FaUser /> Usuario
                    </button>
                    <button
                        type="button"
                        className={loginType === "vet" ? "active" : ""}
                        onClick={() => setLoginType("vet")}
                    >
                        <FaHandHoldingMedical /> Veterinario
                    </button>
                </div>

                {/* Formulario Usuarios */}
                <div className="form-group">
                    <label>{config[loginType].label}</label>
                    <input
                        type="email"
                        name='email'
                        className={error ? 'input-error' : ''}
                        value={credentials.email}
                        onChange={handleChange}
                        required
                        placeholder={config[loginType].placeholder}
                    />
                </div>

                <div className="form-group">
                    <label>Contraseña</label>
                    <input
                        type="password"
                        name='password'
                        className={error ? 'input-error' : ''}
                        value={credentials.password}
                        onChange={handleChange}
                        required
                        placeholder="••••••••"
                    />
                </div>

                <button type="submit">Entrar</button>
            </form>
        </div>
    );
};

export default Login;
