import React from 'react';
import { Outlet, useNavigate } from 'react-router-dom';
import './Layout.css';
import logo from '../assets/logoVet.png';

const Layout = () => {
    const navigate = useNavigate();

    return (
        <div className="app-layout">
            <header className="app-header">
                <div className="logo-container" onClick={() => navigate('/home')}>
                    <img src={logo} alt="VetPet Logo" className="logo-image" />
                    <span className="brand-name">ViperaVet</span>
                </div>
                <nav className="nav-menu">
                    <span className="nav-link" onClick={() => navigate('/search')} style={{cursor: 'pointer'}}>Mascotas</span>
                    <a href="#" className="nav-link">Sobre nosotros</a>
                </nav>
            </header>

            <main className="app-main">
                <Outlet />
            </main>

            <footer className="app-footer">
                <div className="footer-content">
                    <div className="footer-column">
                        <h3>Legal</h3>
                        <p>&copy; 2026 ViperaVet.</p>
                        <p>Todos los derechos reservados.</p>
                    </div>
                    <div className="footer-column">
                        <h3>Contacto</h3>
                        <p>Email: alonsomorosvillalba@gmail.com</p>
                        <p>Teléfono: +34 625 375 615</p>
                        <p>Dirección: Alcobendas, Madrid</p>
                    </div>
                    <div className="footer-column">
                        <h3>Redes Sociales</h3>
                        <a href="https://github.com/alonsomoros" target="_blank" rel="noopener noreferrer">GitHub</a>
                        <a href="https://www.linkedin.com/in/alonso-moros-villalba-301904275/" target="_blank" rel="noopener noreferrer">LinkedIn</a>
                        <a href="https://www.instagram.com/aalonn_11_mv/" target="_blank" rel="noopener noreferrer">Instagram</a>
                    </div>
                </div>
                <div className="copyright">
                    © 2026 ViperaVet System - Cuidando de tus mejores amigos
                </div>
            </footer>
        </div>
    );
};

export default Layout;
