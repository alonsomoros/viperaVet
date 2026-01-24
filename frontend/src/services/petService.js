const extractData = (data) => {
    if (!data) return [];
    if (Array.isArray(data)) return data;
    if (data.content && Array.isArray(data.content)) return data.content;
    return [];
};

export const getSpecies = async () => {
    const response = await fetch('/species');
    if (!response.ok) throw new Error('Error fetching species');
    return await response.json();
};

export const getBreedsBySpecies = async (species) => {
    // Backend expects specie_id parameter for filtering
    const response = await fetch(`/breeds?specie_id=${encodeURIComponent(species)}`);
    if (!response.ok) throw new Error('Error fetching breeds');
    return await response.json();
};

export const searchPets = async (filters = {}) => {
    const params = new URLSearchParams();
    if (filters.name) params.append('name', filters.name);
    if (filters.species) params.append('specie_id', filters.species);
    if (filters.breed) params.append('breed_id', filters.breed);

    const response = await fetch(`/pets?${params.toString()}`);
    if (!response.ok) throw new Error('Error searching pets');
    
    const data = await response.json();
    return extractData(data);
};

/**
{
    "name": "Obi",
    "birthDate": "2022-02-23",
    "specieId": 1,
    "breedId": 37,
    "weight": 26,
    "dietInfo": "Le flipan los filetes"
}
 */
export const registerPet = async (petData, token) => {
    try {
        const response = await fetch('/pets', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`
            },
            body: JSON.stringify(petData),
        });

        if (!response.ok) {
            const errorData = await response.json().catch(() => ({}));
            throw new Error(errorData.message || 'Error al registrar la mascota');
        }

        return await response.json();
    } catch (error) {
        throw error;
    }
};
