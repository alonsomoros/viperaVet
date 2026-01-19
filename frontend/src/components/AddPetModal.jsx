import { useState, useEffect } from 'react';
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
            </div>
        </div>
    );

}

export default AddPetModal;