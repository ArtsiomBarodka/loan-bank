import React from "react";

export const Button = ({ onClick, disabled, text }) => {
  return (
    <button className="btn btn-primary" onClick={onClick} disabled={disabled}>
      {text}
    </button>
  );
};
