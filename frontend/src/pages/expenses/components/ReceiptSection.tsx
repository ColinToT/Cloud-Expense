import { Button, message, Upload } from "antd";
import { UploadOutlined } from "@ant-design/icons";
import type { UploadProps } from "antd";
import { uploadReceipt } from "@/api/receipt";
import type { Receipt } from "@/types/expense";

interface Props {
  expenseId: number;
  receipts: Receipt[];
  editable: boolean;
  onReceiptChanged: () => Promise<void>;
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

      await onReceiptChanged();
    } catch (error) {
      message.error("Failed to upload receipt.");

      onError?.(error as Error);
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
        <p>No receipts attached.</p>
      ) : (
        receipts.map((receipt) => (
          <div key={receipt.id}>{receipt.fileName}</div>
        ))
      )}
    </div>
  );
};

export default ReceiptSection;
