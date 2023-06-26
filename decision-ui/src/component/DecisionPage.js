import React from "react";
import { Input } from "./Input";
import { Button } from "./Button";
import { DecisionResult } from "./DecisionResult";
import { ErrorResult } from "./ErrorResult";
import { fetchDecision } from "../gateway/decision-gateway";

export const DecisionPage = () => {
  const [userCode, setUserCode] = React.useState("");
  const [loanAmount, setLoanAmount] = React.useState("");
  const [loanPeriod, setLoanPeriod] = React.useState("");
  const [decision, setDecision] = React.useState(null);
  const [error, setError] = React.useState(null);
  const [validationErrors, setValidationErrors] = React.useState({});

  const cleanValidationErrors = () => {
    setValidationErrors({});
  };

  const onChangeUserCode = (e) => {
    cleanValidationErrors();
    setUserCode(e.target.value);
  };

  const onChangeLoanAmount = (e) => {
    cleanValidationErrors();
    setLoanAmount(e.target.value);
  };

  const onChangeLoanPeriod = (e) => {
    cleanValidationErrors();
    setLoanPeriod(e.target.value);
  };

  const handleDecisionClick = () => {
    cleanValidationErrors();
    setDecision(null);
    setError(null);

    const body = {
      userCode: userCode,
      loanAmount: loanAmount,
      loanPeriod: loanPeriod,
    };

    fetchDecision(body)
      .then((decision) => {
        setDecision(decision);
      })
      .catch((apiError) => {
        setError(apiError);
        if (apiError.validationCaused) {
          setValidationErrors({ ...apiError.validationCaused });
        }
      });
  };

  let disableSubmit = false;
  if (userCode === "" || loanAmount === "" || loanPeriod === "") {
    disableSubmit = true;
  }

  return (
    <main className="container">
      <h1 className="text-center p-4">Decision Page</h1>
      <div className="row justify-content-around">
        <section className="col-5">
          <h2 className="mb-3">Input</h2>
          <div>
            <Input
              label="User code"
              placeholder="Provide userCode (1,2,3...)"
              type="number"
              hasError={!!validationErrors.userCode}
              error={validationErrors.userCode}
              value={userCode}
              onChange={onChangeUserCode}
            />
            <Input
              label="Loan amount"
              placeholder="Needed loan ammount"
              type="number"
              hasError={!!validationErrors.loanAmount}
              error={validationErrors.loanAmount}
              value={loanAmount}
              onChange={onChangeLoanAmount}
            />
            <Input
              label="Loan period"
              placeholder="Needed loan period"
              type="number"
              hasError={!!validationErrors.loanPeriod}
              error={validationErrors.loanPeriod}
              value={loanPeriod}
              onChange={onChangeLoanPeriod}
            />
          </div>
          <div className="text-center mt-4">
            <Button
              text="Get decision"
              onClick={handleDecisionClick}
              disabled={disableSubmit}
            />
          </div>
        </section>
        <section className="col-5">
          <h2 className="mb-3">Output</h2>
          {decision && <DecisionResult decision={decision} />}
          {error && <ErrorResult error={error} />}
        </section>
      </div>
    </main>
  );
};
