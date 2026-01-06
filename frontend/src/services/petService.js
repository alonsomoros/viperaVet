const API_URL = '/pets'; // Assuming base for pets, or just use relative paths for specific endpoints

export const getSpecies = async () => {
    const response = await fetch('/species');
    if (!response.ok) throw new Error('Error fetching species');
    return await response.json();
};

export const getBreedsBySpecies = async (species) => {
    // Handling generic or specific endpoint based on user prompt instructions "endpoint /breeds o el filtrado por especie que tiene el backend"
    // I'll assume /breeds?species=... or just /breeds and filter, but usually backend filters.
    // Let's try /breeds?species=${species} first as it's cleaner.
    const response = await fetch(`/breeds?specie_id=${encodeURIComponent(species)}`);
    if (!response.ok) throw new Error('Error fetching breeds');
    return await response.json();
};

export const searchPets = async (filters = {}) => {
    // constructing query string
    const params = new URLSearchParams();
    if (filters.name) params.append('name', filters.name);
    if (filters.species) params.append('specie_id', filters.species);
    if (filters.breed) params.append('breed_id', filters.breed);

    const response = await fetch(`/pets?${params.toString()}`);
    if (!response.ok) throw new Error('Error searching pets');
    return await response.json();
};
