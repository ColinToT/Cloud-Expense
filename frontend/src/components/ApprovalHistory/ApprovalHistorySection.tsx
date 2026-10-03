import type { ApprovalHistory as ApprovalHistoryType } from "@/types/approval";

import "./ApprovalHistory.css";

interface Props {
  history: ApprovalHistoryType[];
}

const ApprovalHistorySection = ({ history }: Props) => {
  if (history.length === 0) {
    return null;
  }

  return (
    <section className="approval-history-card">
      <h2>Approval history</h2>

      <div className="approval-history">
        {history.map((record, index) => (
          <div
            className="approval-history__item"
            key={`${record.stage}-${record.createdAt}-${index}`}
          >
            <div className="approval-history__header">
              <div>
                <strong>
                  {record.stage === "MANAGER"
                    ? "Manager review"
                    : "Finance review"}
                </strong>

                <div className="approval-history__meta">
                  {record.action === "APPROVE" ? "Approved" : "Rejected"} by{" "}
                  {record.approverName}
                </div>
              </div>

              <span className="approval-history__date">
                {new Date(record.createdAt).toLocaleString()}
              </span>
            </div>

            {record.comment && (
              <div className="approval-history__comment">{record.comment}</div>
            )}
          </div>
        ))}
      </div>
    </section>
  );
};

export default ApprovalHistorySection;
