import React from 'react';
import { useAuth } from '../context/AuthContext';
import { useNavigate } from 'react-router-dom';
import { FaPlus } from 'react-icons/fa';
import './Home.css';

const Home = () => {
    const { user, logout } = useAuth();
    const navigate = useNavigate();

    const animalImages = [
    'https://images.unsplash.com/photo-1609348490161-a879e4327ae9?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3w3Nzg4Nzd8MHwxfHNlYXJjaHwxfHxoYXBweSUyMGRvZyUyMHBvcnRyYWl0fGVufDF8fHx8MTc2ODUzMjYzNnww&ixlib=rb-4.1.0&q=80&w=1080&utm_source=figma&utm_medium=referral',
    'https://images.unsplash.com/photo-1513360371669-4adf3dd7dff8?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3w3Nzg4Nzd8MHwxfHNlYXJjaHwxfHxjYXQlMjBwZXR8ZW58MXx8fHwxNzY4NTY4NjM3fDA&ixlib=rb-4.1.0&q=80&w=1080&utm_source=figma&utm_medium=referral',
    'https://images.unsplash.com/photo-1695510864636-38ff5ba5a945?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3w3Nzg4Nzd8MHwxfHNlYXJjaHwxfHxyYWJiaXQlMjBwZXR8ZW58MXx8fHwxNzY4NTM2NzE5fDA&ixlib=rb-4.1.0&q=80&w=1080&utm_source=figma&utm_medium=referral',
    'https://images.unsplash.com/photo-1699721770122-c60db6612c44?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3w3Nzg4Nzd8MHwxfHNlYXJjaHwxfHxiaXJkJTIwcGFycm90JTIwcGV0fGVufDF8fHx8MTc2ODYwNjY2MHww&ixlib=rb-4.1.0&q=80&w=1080&utm_source=figma&utm_medium=referral',
    'https://images.unsplash.com/photo-1548412342-98d0d2a49205?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3w3Nzg4Nzd8MHwxfHNlYXJjaHwxfHxoYW1zdGVyJTIwcGV0fGVufDF8fHx8MTc2ODU1Mzc2MHww&ixlib=rb-4.1.0&q=80&w=1080&utm_source=figma&utm_medium=referral',
  ];

    const handleLogout = () => {
        logout();
        navigate('/login');
    };

    return (
        <div className="home-container">
            <div className="user-info">
                <p className="welcome-message">Bienvenido <strong>{user.username}</strong> con rol: <strong>{user.role}</strong></p>
            </div>

            
            <div className="animal-images-container"> 
                <button
              onClick={""} /*() => setIsModalOpen(true)*/
              className="add-pet-button-container"
            >
              <div className="add-pet-button-icon">
                <FaPlus />
              </div>
              <p className="add-pet-button-text">
                Dar de alta nueva mascota
              </p>
            </button>
                {animalImages.map((image, index) => (
              <div
                key={index}
                className="animal-image"
              >
                <img
                  src={image}
                  alt={`Animal ${index + 1}`}
                  className="animal-image"
                />
              </div>
            ))}
            </div>

            <button onClick={handleLogout} className="logout-button">Cerrar Sesión</button>
        </div>
    );
};

export default Home;
