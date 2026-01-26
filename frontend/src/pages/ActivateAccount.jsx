import React, { useState, useEffect } from 'react';
import { useSearchParams, useNavigate } from 'react-router-dom';
import { verifyToken, activateAccount } from '../services/authService';
import './ActivateAccount.css';

const ActivateAccount = () => {
    const [searchParams] = useSearchParams();
    const token = searchParams.get('token');
    const navigate = useNavigate();

    const [status, setStatus] = useState('verifying'); // verifying, valid, invalid, success, error
    const [errorMessage, setErrorMessage] = useState('');
    const [passwords, setPasswords] = useState({
        newPassword: '',
        confirmPassword: ''
    });

    useEffect(() => {
        const checkToken = async () => {
            if (!token) {
                setStatus('invalid');
                setErrorMessage('No se ha proporcionado ningún token de activación.');
                return;
            }

            try {
                await verifyToken(token);
                setStatus('valid');
            } catch (error) {
                setStatus('invalid');
                setErrorMessage(error.message || 'El enlace de activación no es válido o ha expirado.');
            }
        };

        checkToken();
    }, [token]);

    const handleChange = (e) => {
        setPasswords({
            ...passwords,
            [e.target.name]: e.target.value
        });
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        
        if (passwords.newPassword !== passwords.confirmPassword) {
            setErrorMessage('Las contraseñas no coinciden.');
            return;
        }

        if (passwords.newPassword.length < 6) {
            setErrorMessage('La contraseña debe tener al menos 6 caracteres.');
            return;
        }

        setStatus('submitting');
        try {
            await activateAccount({
                token,
                newPassword: passwords.newPassword,
                confirmPassword: passwords.confirmPassword
            });
            setStatus('success');
            setTimeout(() => {
                navigate('/login');
            }, 3000);
        } catch (error) {
            setStatus('valid');
            setErrorMessage(error.message || 'Ocurrió un error al activar tu cuenta. Inténtalo de nuevo.');
        }
    };

    return (
        <div className="activate-container">
            <div className="activate-card">
                <div className="activate-header">
                    <h1>Vipera<span>Vet</span></h1>
                    <p>Activación de Cuenta</p>
                </div>

                {status === 'verifying' && (
                    <div className="activate-body status-verifying">
                        <div className="spinner"></div>
                        <p>Verificando tu enlace de activación...</p>
                    </div>
                )}

                {status === 'invalid' && (
                    <div className="activate-body status-invalid">
                        <div className="icon-error">✕</div>
                        <h2>Enlace no válido</h2>
                        <p>{errorMessage}</p>
                        <button className="btn-primary" onClick={() => navigate('/login')}>
                            Volver al Login
                        </button>
                    </div>
                )}

                {(status === 'valid' || status === 'submitting') && (
                    <div className="activate-body">
                        <h2>Establece tu contraseña</h2>
                        <p>Para completar la activación de tu cuenta, elige una contraseña segura.</p>
                        
                        <form onSubmit={handleSubmit} className="activate-form">
                            <div className="form-group">
                                <label htmlFor="newPassword">Nueva Contraseña</label>
                                <input
                                    type="password"
                                    id="newPassword"
                                    name="newPassword"
                                    value={passwords.newPassword}
                                    onChange={handleChange}
                                    placeholder="Mínimo 6 caracteres"
                                    required
                                    disabled={status === 'submitting'}
                                />
                            </div>
                            <div className="form-group">
                                <label htmlFor="confirmPassword">Confirmar Contraseña</label>
                                <input
                                    type="password"
                                    id="confirmPassword"
                                    name="confirmPassword"
                                    value={passwords.confirmPassword}
                                    onChange={handleChange}
                                    placeholder="Repite tu contraseña"
                                    required
                                    disabled={status === 'submitting'}
                                />
                            </div>
                            
                            {errorMessage && <p className="error-text">{errorMessage}</p>}
                            
                            <button 
                                type="submit" 
                                className="btn-primary" 
                                disabled={status === 'submitting'}
                            >
                                {status === 'submitting' ? 'Activando...' : 'Activar Cuenta'}
                            </button>
                        </form>
                    </div>
                )}

                {status === 'success' && (
                    <div className="activate-body status-success">
                        <div className="icon-success">✓</div>
                        <h2>¡Cuenta Activada!</h2>
                        <p>Tu cuenta ha sido activada correctamente. Ahora puedes iniciar sesión con tu nueva contraseña.</p>
                        <p className="redirect-text">Redirigiendo al login...</p>
                        <button className="btn-primary" onClick={() => navigate('/login')}>
                            Ir al Login ahora
                        </button>
                    </div>
                )}
            </div>
        </div>
    );
};

export default ActivateAccount;
