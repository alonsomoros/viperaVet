import React, { useState, useEffect } from 'react';
import {
    FaTimes,
    FaCheck,
    FaExclamationCircle,
    FaUser,
    FaPaw,
    FaSearch,
    FaSpinner
} from 'react-icons/fa';
import { toast } from 'sonner';
import './AddPetModale.css';

const AddPetModal = ({ isOpen, onClose }) => {

    // --- ESTADOS ---
    const [currentStep, setCurrentStep] = React.useState(1);

    const nextStep = () => setCurrentStep(currentStep + 1);
    const prevStep = () => setCurrentStep(currentStep - 1);

    // --- FORM DATA ---
    const [formData, setFormData] = useState({
        // Mascota
        petName: '',
        petBirthDate: '',
        petSpecies: '',
        petBreed: '',
        petAllergies: '',
        petDiet: '',
        // Usuario (Dueño)
        userEmail: '',
        userDni: '',
        userPhone: '',
        userName: ''
    });

    // --- HANDLERS ---

    // Overlay click handler
    const handleOverlayClick = (e) => {
        if (e.target.className === 'modal-overlay') {
            onClose();
        }
    };

    // Handle change
    const handleChange = (e) => {
        setFormData({ ...formData, [e.target.name]: e.target.value });
    };

    // --- CHECK USER EXISTS ---
    const checkUserExists = async () => {
    };

    // --- NEXT STEP ---
    const handleNextStep = () => {
    };

    // --- SUBMIT ---
    const handleSubmit = () => {

    };

    // --- RESET FORM ---
    const resetForm = () => {
        setCurrentStep(1);
        setUserExists(null);
        setFormData({
            petName: '', petBirthDate: '', petSpecies: '', petBreed: '', petAllergies: '', petDiet: '',
            userEmail: '', userDni: '', userPhone: '', userName: ''
        });
    };

    if (!isOpen) return null;

    return (
        <div className="modal-overlay" onClick={handleOverlayClick}>
            <div className="modal-container">
                <div className="modal-header">
                    <h2>Agregar nueva mascota</h2>
                    <button onClick={onClose} className="close-button">
                        <FaTimes />
                    </button>
                </div>
                <hr />
                <div className='modal-steps'>
                    {currentStep === 1 && <h2>Mascota</h2>}
                    {currentStep === 2 && <h2>Usuario</h2>}
                </div>
                <hr />
                <div className="modal-body">
                    {currentStep === 1 && (
                        <div className='modal-pet-form-grid'>
                            <div className='modal-pet-form-grid-item'>
                                <label className='required' htmlFor="petName">Nombre</label>
                                <input
                                    type="text"
                                    name="petName"
                                    placeholder="Nombre de la mascota"
                                    value={formData.petName}
                                    onChange={handleChange}
                                    required
                                />
                            </div>
                            <div className='modal-pet-form-grid-item'>
                                <label className='required' htmlFor="petBirthDate">Fecha de nacimiento</label>
                                <input
                                    type="date"
                                    name="petBirthDate"
                                    placeholder="Fecha de nacimiento"
                                    value={formData.petBirthDate}
                                    max={new Date().toISOString().split('T')[0]}
                                    onChange={handleChange}
                                    required
                                />
                            </div>
                            <div className='modal-pet-form-grid-item'>
                                <label className='required' htmlFor="petSpecies">Especie</label>
                                <input
                                    type="text"
                                    name="petSpecies"
                                    placeholder="Especie"
                                    value={formData.petSpecies}
                                    onChange={handleChange}
                                    required
                                />
                            </div>
                            <div className='modal-pet-form-grid-item'>
                                <label className='required' htmlFor="petBreed">Raza</label>
                                <input
                                    type="text"
                                    name="petBreed"
                                    placeholder="Raza de la mascota"
                                    value={formData.petBreed}
                                    onChange={handleChange}
                                    required
                                />
                            </div>
                            <div className='modal-pet-form-grid-item full-width'>
                                <label htmlFor="petAllergies">Alergias</label>
                                <input
                                    type="text"
                                    name="petAllergies"
                                    placeholder="Alergias conocidas"
                                    value={formData.petAllergies}
                                    onChange={handleChange}
                                />
                            </div>
                            <div className='modal-pet-form-grid-item full-width'>
                                <label htmlFor="petDiet">Dieta Alimenticia</label>
                                <input
                                    type="text"
                                    name="petDiet"
                                    placeholder="Información sobre la dieta"
                                    value={formData.petDiet}
                                    onChange={handleChange}
                                />
                            </div>
                        </div>
                    )}
                    {currentStep === 2 && (
                        <>
                            <label htmlFor="userEmail">Correo del Usuario</label>
                            <input
                                type="text"
                                name="userEmail"
                                placeholder="correo@ejemplo.com"
                                value={formData.userEmail}
                                onChange={handleChange}
                            />
                            <button onClick={checkUserExists}>Verificar</button>
                        </>
                    )}
                </div>
                <hr />
                <div className="modal-footer">
                    {currentStep > 1 && <button onClick={prevStep}>Atrás</button>}
                    {currentStep < 2 && <button onClick={nextStep}>Siguiente</button>}
                </div>
            </div>
        </div>
    );

}

export default AddPetModal;