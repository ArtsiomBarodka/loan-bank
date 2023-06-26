import React from "react";

export const ErrorResult = ({ error }) => {
  const { message, caused } = error;

  return (
    <>
      <h6>Error occurred:</h6>
      <div>Message: {message}</div>
      {caused && <div>Caused: {caused}</div>}
    </>
  );
};
