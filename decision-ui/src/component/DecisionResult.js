import React from "react";

export const DecisionResult = ({ decision }) => {
  const {
    userCode,
    isApproved,
    declineReason,
    requestedLoanInfo,
    minPeriodLoadInfo,
    maxAmountLoadInfo,
  } = decision;

  return (
    <>
      <div>User code: {userCode}</div>
      <h6 className="pt-4">Requested loan info:</h6>
      <div>
        Amount = {requestedLoanInfo.amount}; Period = {requestedLoanInfo.period}
      </div>
      <div>
        Loan is {isApproved ? "approved." : "declined."}
        {declineReason && <span> Reason: {declineReason}</span>}
      </div>
      {minPeriodLoadInfo && (
        <div>
          <h6 className="pt-4">
            Min possible period for requested loan amount:
          </h6>
          Amount = {minPeriodLoadInfo.amount}; Period =
          {minPeriodLoadInfo.period}
        </div>
      )}
      {maxAmountLoadInfo && (
        <div>
          <h6 className="pt-4">
            Max possible amount for requested loan period:
          </h6>
          Amount = {maxAmountLoadInfo.amount}; Period =
          {maxAmountLoadInfo.period}
        </div>
      )}
    </>
  );
};
