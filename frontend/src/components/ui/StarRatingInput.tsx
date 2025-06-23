// src/components/ui/StarRatingInput.tsx
"use client"
import React, { useState } from 'react';

interface StarRatingInputProps {
    rating: number;
    setRating: (rating: number) => void;
    totalStars?: number;
}

const StarRatingInput: React.FC<StarRatingInputProps> = ({ rating, setRating, totalStars = 5 }) => {
    const [hover, setHover] = useState(0);

    return (
        <div className="flex space-x-1">
            {[...Array(totalStars)].map((_, index) => {
                const starValue = index + 1;
                return (
                    <button
                        type="button"
                        key={starValue}
                        className={`text-3xl transition-colors ${starValue <= (hover || rating) ? 'text-yellow-400' : 'text-gray-600'}`}
                        onClick={() => setRating(starValue)}
                        onMouseEnter={() => setHover(starValue)}
                        onMouseLeave={() => setHover(0)}
                    >
                        &#9733;
                    </button>
                );
            })}
        </div>
    );
};

export default StarRatingInput;