import React, { useEffect, useState } from 'react';
import { FaTimes, FaSpinner, FaSearch } from 'react-icons/fa';
import { toast } from 'sonner';
import { useAuth } from '../context/AuthContext';
import { registerUser } from '../services/authService';
import { registerPet, getSpecies, getBreedsBySpecies } from '../services/petService';
import { getUserByEmail } from '../services/userService';
import './AddPetModale.css';

const AddPetModal = ({ isOpen, onClose }) => {
    const { user } = useAuth();

    // --- STATES ---
    const [currentStep, setCurrentStep] = useState(1);
    const [isCheckingEmail, setIsCheckingEmail] = useState(false);
    const [speciesList, setSpeciesList] = useState([]);
    const [breedsList, setBreedsList] = useState([]);
    const [userExists, setUserExists] = useState(null);

    // --- FORM DATA ---
    const [formData, setFormData] = useState({
        // Pet Data
        petName: '',
        petBirthDate: '',
        petSpecies: '',
        petBreed: '',
        petWeight: '',
        petAllergies: '',
        petDiet: '',
        // User Data
        userEmail: '',
        userDni: '',
        userPhone: '',
        userName: ''
    });

    // CARGA DE ESPECIES
    useEffect(() => {
        const loadSpecies = async () => {
            try {
                const data = await getSpecies();
                // Si el backend devuelve { content: [...] }, asegúrate de extraer el array
                setSpeciesList(Array.isArray(data) ? data : data.content || []);
            } catch (error) {
                toast.error("Error al cargar especies");
            }
        };
        if (isOpen) loadSpecies();
    }, [isOpen]);

    // CARGA DE RAZAS
    useEffect(() => {
        const loadBreeds = async () => {
            if (!formData.petSpecies) {
                setBreedsList([]);
                return;
            }
            try {
                const data = await getBreedsBySpecies(formData.petSpecies);
                setBreedsList(Array.isArray(data) ? data : data.content || []);
            } catch (error) {
                console.error("Error cargando razas", error);
            }
        };
        loadBreeds();
    }, [formData.petSpecies]);

    // --- HANDLERS ---

    const handleOverlayClick = (e) => {
        if (e.target.className === 'modal-overlay') {
            handleClose();
        }
    };

    const handleChange = (e) => {
        const { name, value } = e.target;
        // Reset verification if email changes
        if (name === 'userEmail') {
            setUserExists(null);
        }

        // Reset breed if species changes
        if (name === 'petSpecies') {
            setFormData(prev => ({
                ...prev,
                petSpecies: value,
                petBreed: '' // Reset selection
            }));
            return;
        }

        setFormData({ ...formData, [name]: value });
    };

    // --- CHECK USER EXISTS ---
    const checkUserExists = async () => {
        if (!formData.userEmail) {
            return toast.error('Ingresa un correo electrónico');
        }

        setIsCheckingEmail(true);
        try {
            const token = user?.token; // Get token from auth context
            console.log("Token sending:", token); // Debug info

            const response = await getUserByEmail(formData.userEmail, token);
            const exists = response?.content?.length > 0;
            setUserExists(exists);

            if (exists) {
                toast.success('Usuario encontrado. Se vinculará automáticamente.');
                // We might want to fill user data if returned?
                setFormData(prev => ({
                    ...prev,
                    userName: response.content[0].username || '',
                }));
            } else {
                toast.info('Usuario no encontrado. Por favor completa los datos del propietario.');
            }
        } catch (error) {
            console.error("Error checking email:", error);
            toast.error(error.message || 'Error al verificar el usuario');
            setUserExists(null);
        } finally {
            setIsCheckingEmail(false);
        }
    };

    // --- VALIDATION ---
    const validateStep1 = () => {
        const { petName, petBirthDate, petSpecies, petBreed } = formData;
        if (!petName || !petBirthDate || !petSpecies || !petBreed) {
            toast.error('Por favor, completa todos los campos obligatorios (*)');
            return false;
        }
        return true;
    };

    const validateNewUser = () => {
        const { userName, userDni, userPhone } = formData;
        if (!userName || !userDni || !userPhone) {
            toast.error('Por favor, completa todos los datos del nuevo propietario');
            return false;
        }
        return true;
    };

    // --- NAVIGATION ---
    const handleNextStep = () => {
        if (currentStep === 1) {
            if (validateStep1()) {
                setCurrentStep(2);
            }
        } else if (currentStep === 2) {
            if (userExists === null) {
                toast.error('Por favor, verifica el correo electrónico del usuario primero.');
                return;
            }

            // If user exists, we can submit directly
            if (userExists === true) {
                submitFinalData();
            }
            // If user likely new, validate their details then submit
            else if (userExists === false) {
                if (validateNewUser()) {
                    submitFinalData();
                }
            }
        }
    };

    const submitFinalData = async () => {
        console.log("Enviando datos finales:", formData);
        const token = user?.token;
        const petData = {
            name: formData.petName,
            birthDate: formData.petBirthDate,
            specieId: formData.petSpecies ? Number(formData.petSpecies) : null,
            breedId: formData.petBreed ? Number(formData.petBreed) : null,
            weight: formData.petWeight ? Number(formData.petWeight) : 0,
            dietInfo: formData.petDiet,
            email: formData.userEmail // Enviar el correo del dueño
        };
        try {
            // si userExists es true, POST a /pets
            if (userExists) {
                await registerPet(petData, token);
            } 
            // si userExists es false, POST a /users y luego POST a /pets
            else {
                const userData = {
                    name: formData.userName,
                    surnames: "Owner",
                    email: formData.userEmail,
                    phone: formData.userPhone,
                    dni: formData.userDni,
                    role: "USER"
                };
                await registerUser(userData);
                await registerPet(petData, token);
            }
            toast.success("Mascota registrada correctamente");
            handleClose();
        } catch (error) {
            console.error("Error en el registro:", error);
            toast.error(error.message || "Error al completar el registro");
        }
    };

    const handleClose = () => {
        resetAll();
        onClose();
    };

    const resetAll = () => {
        setCurrentStep(1);
        setUserExists(null);
        setIsCheckingEmail(false);
        setFormData({
            petName: '', petBirthDate: '', petSpecies: '', petBreed: '', petWeight: '', petAllergies: '', petDiet: '',
            userEmail: '', userDni: '', userPhone: '', userName: ''
        });
    };

    if (!isOpen) return null;

    return (
        <div className="modal-overlay" onClick={handleOverlayClick}>
            <div className="modal-container">
                {/* Header */}
                <div className="modal-header">
                    <h2>Agregar nueva mascota</h2>
                    <button onClick={handleClose} className="close-button">
                        <FaTimes />
                    </button>
                </div>

                {/* Step Indicator */}
                <div className="step-indicator">
                    <div className={`step-item ${currentStep >= 1 ? 'active' : ''}`}>
                        <div className="step-circle">1</div>
                        <span>Mascota</span>
                    </div>
                    <div className={`step-line ${currentStep >= 2 ? 'active' : ''}`}></div>
                    <div className={`step-item ${currentStep >= 2 ? 'active' : ''}`}>
                        <div className="step-circle">2</div>
                        <span>Usuario</span>
                    </div>
                </div>

                {/* Body */}
                <div className="modal-body">
                    {/* STEP 1: PET INFO */}
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
                                />
                            </div>
                            <div className='modal-pet-form-grid-item'>
                                <label className='required' htmlFor="petBirthDate">Fecha de nacimiento</label>
                                <input
                                    type="date"
                                    name="petBirthDate"
                                    value={formData.petBirthDate}
                                    max={new Date().toISOString().split('T')[0]}
                                    onChange={handleChange}
                                />
                            </div>
                            <div className='modal-pet-form-grid-item'>
                                <label className='required' htmlFor="petSpecies">Especie</label>
                                <select
                                    name="petSpecies"
                                    value={formData.petSpecies}
                                    onChange={handleChange}
                                >
                                    <option value="">Seleccionar especie...</option>
                                    {speciesList.map(specie => (
                                        <option key={specie.id} value={specie.id}>
                                            {specie.name}
                                        </option>
                                    ))}
                                </select>
                            </div>
                            <div className='modal-pet-form-grid-item'>
                                <label className='required' htmlFor="petBreed">Raza</label>
                                <select
                                    name="petBreed"
                                    value={formData.petBreed}
                                    onChange={handleChange}
                                    disabled={!formData.petSpecies}
                                >
                                    <option value="">Seleccionar raza...</option>
                                    {breedsList.map(breed => (
                                        <option key={breed.id} value={breed.id}>
                                            {breed.name}
                                        </option>
                                    ))}
                                </select>
                            </div>
                            <div className='modal-pet-form-grid-item'>
                                <label htmlFor="petWeight">Peso (kg)</label>
                                <input
                                    type="number"
                                    name="petWeight"
                                    step="0.1"
                                    placeholder="0.0"
                                    value={formData.petWeight}
                                    onChange={handleChange}
                                />
                            </div>
                            <div className='modal-pet-form-grid-item full-width'>
                                <label htmlFor="petAllergies">Alergias</label>
                                <input
                                    type="text"
                                    name="petAllergies"
                                    placeholder="Alergias conocidas (opcional)"
                                    value={formData.petAllergies}
                                    onChange={handleChange}
                                />
                            </div>
                            <div className='modal-pet-form-grid-item full-width'>
                                <label htmlFor="petDiet">Dieta Alimenticia</label>
                                <input
                                    type="text"
                                    name="petDiet"
                                    placeholder="Información sobre la dieta (opcional)"
                                    value={formData.petDiet}
                                    onChange={handleChange}
                                />
                            </div>
                        </div>
                    )}

                    {/* STEP 2: USER INFO */}
                    {currentStep === 2 && (
                        <div className="step-2-content">
                            <label htmlFor="userEmail" className="required">Correo del Propietario</label>
                            <div className="verify-container">
                                <input
                                    type="email"
                                    name="userEmail"
                                    placeholder="correo@ejemplo.com"
                                    value={formData.userEmail}
                                    onChange={handleChange}
                                />
                                <button
                                    className="btn-verify"
                                    onClick={checkUserExists}
                                    disabled={isCheckingEmail || !formData.userEmail}
                                >
                                    {isCheckingEmail ? <FaSpinner className="spinner" /> : <FaSearch />}
                                    {isCheckingEmail ? 'Verificando...' : 'Verificar'}
                                </button>
                            </div>

                            {/* User Found State */}
                            {userExists === true && (
                                <div className="user-status-alert alert-success">
                                    <strong>✓ Usuario verificado.</strong> La mascota se asociará a este usuario existente.
                                </div>
                            )}

                            {/* User Not Found State (New User Form) */}
                            {userExists === false && (
                                <div className="new-user-form">
                                    <div className="user-status-alert alert-info">
                                        <strong>Nuevo Propietario.</strong> El usuario no existe. Por favor, completa sus datos básicos. Se le enviará un correo para terminar el registro.
                                    </div>

                                    <label className="required">Nombre Completo</label>
                                    <input
                                        type="text"
                                        name="userName"
                                        placeholder="Nombre y Apellidos"
                                        value={formData.userName}
                                        onChange={handleChange}
                                    />

                                    <div className="modal-pet-form-grid">
                                        <div className="modal-pet-form-grid-item">
                                            <label className="required">DNI / NIF</label>
                                            <input
                                                type="text"
                                                name="userDni"
                                                placeholder="12345678X"
                                                value={formData.userDni}
                                                onChange={handleChange}
                                            />
                                        </div>
                                        <div className="modal-pet-form-grid-item">
                                            <label className="required">Teléfono</label>
                                            <input
                                                type="tel"
                                                name="userPhone"
                                                placeholder="600123456"
                                                value={formData.userPhone}
                                                onChange={handleChange}
                                            />
                                        </div>
                                    </div>
                                </div>
                            )}
                        </div>
                    )}
                </div>

                {/* Footer Buttons */}
                <div className="modal-footer">
                    <button
                        className="btn-back"
                        onClick={() => setCurrentStep(currentStep - 1)}
                        style={{ visibility: currentStep === 1 ? 'hidden' : 'visible' }}
                    >
                        Atrás
                    </button>
                    <button
                        className="btn-next"
                        onClick={handleNextStep}
                        disabled={currentStep === 2 && userExists === null}
                    >
                        {currentStep === 2 ? 'Finalizar' : 'Siguiente'}
                    </button>
                </div>
            </div>
        </div>
    );
}

export default AddPetModal;