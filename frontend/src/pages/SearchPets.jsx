import React, { useState, useEffect } from 'react';
import { getSpecies, getBreedsBySpecies, searchPets } from '../services/petService';
import './SearchPets.css';

const SearchPets = () => {
    const [searchTerm, setSearchTerm] = useState('');
    const [speciesList, setSpeciesList] = useState([]);
    const [selectedSpecies, setSelectedSpecies] = useState('');
    const [breedList, setBreedList] = useState([]);
    const [selectedBreed, setSelectedBreed] = useState('');
    const [pets, setPets] = useState([]);
    
    // UI States
    const [isLoadingSpecies, setIsLoadingSpecies] = useState(false);
    const [isLoadingBreeds, setIsLoadingBreeds] = useState(false);
    const [isLoadingPets, setIsLoadingPets] = useState(false);
    const [error, setError] = useState(null);

    // Initial load: Get Species and initial Pets
    useEffect(() => {
        const fetchInitialData = async () => {
            setIsLoadingSpecies(true);
            setIsLoadingPets(true);
            try {
                const [speciesData, petsData] = await Promise.all([
                    getSpecies(),
                    searchPets()
                ]);
                setSpeciesList(speciesData);
                setPets(petsData);
            } catch (err) {
                console.error("Error fetching initial data", err);
                setError("Error al cargar datos iniciales");
            } finally {
                setIsLoadingSpecies(false);
                setIsLoadingPets(false);
            }
        };

        fetchInitialData();
    }, []);

    // When species changes, fetch breeds
    useEffect(() => {
        if (!selectedSpecies) {
            setBreedList([]);
            setSelectedBreed('');
            return;
        }

        const fetchBreeds = async () => {
            setIsLoadingBreeds(true);
            try {
                const breeds = await getBreedsBySpecies(selectedSpecies);
                setBreedList(breeds);
            } catch (err) {
                console.error("Error fetching breeds", err);
            } finally {
                setIsLoadingBreeds(false);
            }
        };

        fetchBreeds();
    }, [selectedSpecies]);

    // Handle Search/Filter
    const handleSearch = async () => {
        setIsLoadingPets(true);
        setError(null);
        try {
            const results = await searchPets({
                name: searchTerm,
                species: selectedSpecies,
                breed: selectedBreed
            });
            setPets(results);
        } catch (err) {
            setError("Error en la búsqueda");
        } finally {
            setIsLoadingPets(false);
        }
    };

    // Real-time search debounce could be added, but user said "o al pulsar buscar". 
    // I'll add a trigger on key enter or button click for now, but also trigger on filter change? 
    // User said "Un input que filtre por nombre de mascota en tiempo real (o al pulsar buscar)". 
    // Let's do simple button for now to match "o al pulsar buscar" strictly and avoid complexity, 
    // but maybe trigger on select change is good UX.

    // Let's verify instructions: "en tiempo real (o al pulsar buscar)". 
    // I will implement useEffect to trigger search when filters change? 
    // Or just a search button. Let's do a search button for the text, but maybe auto-refresh on selects?
    // Actually simpler is just one Search button or useEffect on all dependency changes. 
    // Given "tiempo real", I'll put a useEffect on search term with debounce if I wanted, but let's stick to simple first:
    // I will expose a button, but also maybe useEffect for filters.
    // Let's do a manual search button to remain robust and simple.

    return (
        <div className="search-pets-container">
            <h2>Buscador de Mascotas</h2>
            
            <div className="filters-bar">
                <input 
                    type="text" 
                    placeholder="Buscar por nombre..." 
                    value={searchTerm}
                    onChange={(e) => setSearchTerm(e.target.value)}
                    className="search-input"
                />

                <select 
                    value={selectedSpecies} 
                    onChange={(e) => setSelectedSpecies(e.target.value)}
                    disabled={isLoadingSpecies}
                    className="filter-select"
                >
                    <option value="">Todas las Especies</option>
                    {speciesList.map(s => (
                        <option key={s.id || s} value={s.id || s}>{s.name || s}</option>
                    ))}
                </select>

                <select 
                    value={selectedBreed} 
                    onChange={(e) => setSelectedBreed(e.target.value)}
                    disabled={!selectedSpecies || isLoadingBreeds}
                    className="filter-select"
                >
                    <option value="">Todas las Razas</option>
                    {breedList.map(b => (
                        <option key={b.id || b} value={b.id || b}>{b.name || b}</option>
                    ))}
                </select>

                <button onClick={handleSearch} className="search-button">
                    Buscar
                </button>
            </div>

            {error && <div className="error-msg">{error}</div>}

            {isLoadingPets ? (
                <div className="loading">Cargando mascotas...</div>
            ) : (
                <div className="pets-grid">
                    {pets.length > 0 ? pets.map(pet => (
                        <div key={pet.id} className="pet-card">
                            <div className="pet-image-placeholder">🐶</div>
                            <h3>{pet.name}</h3>
                            <p><strong>Especie:</strong> {pet.specie?.name || 'N/A'}</p>
                            <p><strong>Raza:</strong> {pet.breed?.name || 'N/A'}</p>
                        </div>
                    )) : (
                        <p>No se encontraron mascotas.</p>
                    )}
                </div>
            )}
        </div>
    );
};

export default SearchPets;
