import { Button, message, Upload } from "antd";
import { UploadOutlined } from "@ant-design/icons";
import type { UploadProps } from "antd";
import { deleteReceipt, getReceiptFile, uploadReceipt } from "@/api/receipt";
import type { Receipt } from "@/types/expense";

interface Props {
  expenseId: number;
  receipts: Receipt[];
  editable?: boolean;
  onReceiptChanged?: () => Promise<void>;
}

const ReceiptSection = ({
  expenseId,
  receipts,
  editable,
  onReceiptChanged,
}: Props) => {
  const handleUpload: UploadProps["customRequest"] = async ({
    file,
    onSuccess,
    onError,
  }) => {
    try {
      await uploadReceipt(expenseId, file as File);

      message.success("Receipt uploaded successfully.");

      onSuccess?.("ok");

      if (onReceiptChanged) {
        await onReceiptChanged();
      }
    } catch (error) {
      message.error("Failed to upload receipt.");

      onError?.(error as Error);
    }
  };

  const handleViewReceipt = async (receiptId: number) => {
    try {
      const blob = await getReceiptFile(receiptId);

      const url = URL.createObjectURL(blob);

      window.open(url, "_blank");

      setTimeout(() => {
        URL.revokeObjectURL(url);
      }, 60_000);
    } catch {
      message.error("Failed to open receipt.");
    }
  };

  const handleDeleteReceipt = async (receiptId: number) => {
    try {
      await deleteReceipt(receiptId);

      message.success("Receipt deleted successfully.");

      await onReceiptChanged?.();
    } catch {
      message.error("Failed to delete receipt.");
    }
  };

  return (
    <div className="expense-detail-card">
      <div className="receipt-section__header">
        <h2>Receipts</h2>

        {editable && (
          <Upload customRequest={handleUpload} showUploadList={false}>
            <Button icon={<UploadOutlined />}>Upload receipt</Button>
          </Upload>
        )}
      </div>

      {receipts.length === 0 ? (
        <p className="receipt-section__empty">No receipts attached.</p>
      ) : (
        <div className="receipt-section__list">
          {receipts.map((receipt) => (
            <div className="receipt-section__item" key={receipt.id}>
              <span className="receipt-section__file-name">
                {receipt.fileName}
              </span>

              <Button type="link" onClick={() => handleViewReceipt(receipt.id)}>
                View
              </Button>

              {editable && (
                <Button
                  type="link"
                  danger
                  onClick={() => handleDeleteReceipt(receipt.id)}
                >
                  Delete
                </Button>
              )}
            </div>
          ))}
        </div>
      )}
    </div>
  );
};

export default ReceiptSection;
