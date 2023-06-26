import React from "react";

export const Input = ({
  hasError,
  label,
  type,
  placeholder,
  value,
  onChange,
  error,
}) => {
  let inputClassName = "form-control";
  hasError && (inputClassName += " is-invalid");

  return (
    <>
      {label && <label>{label}</label>}

      <input
        className={inputClassName}
        type={type || "text"}
        placeholder={placeholder}
        value={value}
        onChange={onChange}
      />

      {hasError && <span className="invalid-feedback">{error}</span>}
    </>
  );
};
