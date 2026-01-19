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
        petAge: '',
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
    const handleOverlayClick = (e) => {
        if (e.target.className === 'modal-overlay') {
          onClose();
        }
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
            petName: '', petAge: '', petSpecies: '', petBreed: '', petAllergies: '', petDiet: '',
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
                    {currentStep === 1 && <h3>Body Mascota</h3>}
                    {currentStep === 2 && <h3>Body Usuario</h3>}
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