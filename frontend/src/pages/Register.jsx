import React, { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import { registerUser } from '../services/authService';
import { useAuth } from '../context/AuthContext';
import './Register.css';

const Register = () => {
    const [formData, setFormData] = useState({
        username: '',
        password: '',
        email: '',
        phone: '',
        address: '',
        role: 'USER'
    });
    const [errors, setErrors] = useState({}); // Changed to object for field errors
    const navigate = useNavigate();
    const { login } = useAuth();

    const handleChange = (e) => {
        setFormData({
            ...formData,
            [e.target.name]: e.target.value
        });
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        setErrors({});
        try {
            const data = await registerUser(formData);
            // Auto-login
            login(data);
            navigate('/home');
        } catch (err) {
            // err is expected to be { field: 'message' } or just a generic object
            if (err.global || (!err.email && !err.username && !err.password && !err.phone)) {
                 setErrors({ global: err.global || 'Error desconocido en el registro' });
            } else {
                 setErrors(err);
            }
        }
    };

    return (
        <div className="register-container">
            <form className="register-form" onSubmit={handleSubmit}>
                <h2>Crear Cuenta</h2>
                {errors.global && <p className="error-message global-error">{errors.global}</p>}
                
                <div className="form-group">
                    <label>Nombre de Usuario (Único) *</label>
                    <input 
                        type="text" 
                        name="username"
                        className={errors.username ? 'input-error' : ''}
                        value={formData.username}
                        onChange={handleChange}
                        required 
                        minLength={3}
                        maxLength={50}
                        placeholder='usuario'
                    />
                    {errors.username && <span className="field-error-text">{errors.username}</span>}
                </div>

                <div className="form-group">
                    <label>Email *</label>
                    <input 
                        type="email" 
                        name="email"
                        className={errors.email ? 'input-error' : ''}
                        value={formData.email}
                        onChange={handleChange}
                        required 
                        placeholder='email@gmail.com'
                    />
                    {errors.email && <span className="field-error-text">{errors.email}</span>}
                </div>

                <div className="form-group">
                    <label>Teléfono *</label>
                    <input 
                        type="tel" 
                        name="phone"
                        className={errors.phone ? 'input-error' : ''}
                        value={formData.phone}
                        onChange={handleChange}
                        required 
                        placeholder="654654654"
                    />
                    {errors.phone && <span className="field-error-text">{errors.phone}</span>}
                </div>

                <div className="form-group">
                    <label>Dirección</label>
                    <input 
                        type="text" 
                        name="address"
                        value={formData.address}
                        onChange={handleChange}
                        maxLength={100}
                        placeholder='Calle calle 123, Ciudad, País'
                    />
                </div>

                <div className="form-group">
                    <label>Contraseña * (mín. 6 caracteres)</label>
                    <input 
                        type="password" 
                        name="password"
                        className={errors.password ? 'input-error' : ''}
                        value={formData.password}
                        onChange={handleChange}
                        required 
                        minLength={6}
                        placeholder='Contraseña'
                    />
                    {errors.password && <span className="field-error-text">{errors.password}</span>}
                </div>

                <div className="form-group">
                    <label>Rol</label>
                    <select 
                        name="role" 
                        value={formData.role} 
                        onChange={handleChange}
                    >
                        <option value="USER">Dueño de Mascota (User)</option>
                        <option value="VET">Veterinario (Vet)</option>
                    </select>
                </div>

                <button type="submit">Registrarse</button>
                <p className="login-link">
                    ¿Ya tienes cuenta? <Link to="/login">Inicia sesión aquí</Link>
                </p>
            </form>
        </div>
    );
};

export default Register;
