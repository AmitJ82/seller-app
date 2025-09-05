  import  React, { useState, useEffect } from 'react';

  const CsvUploadModal = ({ isOpen, onClose, type, onUploadSuccess }) => {
  const [file, setFile] = useState(null);
  const [uploading, setUploading] = useState(false);
  const [result, setResult] = useState(null);

  const handleFileChange = (e) => {
    setFile(e.target.files[0]);
    setResult(null);
  };

  const handleUpload = async () => {
    if (!file) {
      alert('Please select a file');
      return;
    }

    setUploading(true);
    try {
      const response = await api.uploadFile(`/csv/upload/${type}`, file);
      setResult(response);
      if (response.successfulRows > 0) {
        onUploadSuccess();
      }
    } catch (error) {
      alert('Upload failed');
    } finally {
      setUploading(false);
    }
  };

    const downloadTemplate = async () => {
    try {
      const blob = await api.downloadTemplate(`/csv/template/${type}`);
      const url = window.URL.createObjectURL(blob);
      const a = document.createElement('a');
      a.href = url;
      a.download = `${type}_template.csv`;
      document.body.appendChild(a);
      a.click();
      document.body.removeChild(a);
      window.URL.revokeObjectURL(url);
    } catch (error) {
      alert('Failed to download template');
    }
  };
   const resetModal = () => {
      setFile(null);
      setResult(null);
      setUploading(false);
      onClose();
    };

    if (!isOpen) return null;

    return (
      <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
        <div className="bg-white p-6 rounded-lg w-full max-w-2xl max-h-96 overflow-y-auto">
          <div className="flex justify-between items-center mb-4">
            <h2 className="text-xl font-bold">Upload {type.charAt(0).toUpperCase() + type.slice(1)} CSV</h2>
            <button onClick={resetModal} className="text-gray-500 hover:text-gray-700">
              <X size={24} />
            </button>
          </div>

          <div className="mb-4">
            <button
              onClick={downloadTemplate}
              className="bg-green-500 hover:bg-green-600 text-white px-4 py-2 rounded-lg flex items-center gap-2 mb-4"
            >
              <Download size={20} />
              Download Template
            </button>

            <div className="border-2 border-dashed border-gray-300 rounded-lg p-6 text-center">
              <input
                type="file"
                accept=".csv"
                onChange={handleFileChange}
                className="hidden"
                id="csvFile"
              />
              <label htmlFor="csvFile" className="cursor-pointer">
                <Upload className="mx-auto h-12 w-12 text-gray-400 mb-2" />
                <p className="text-sm text-gray-600">
                  {file ? file.name : 'Click to select a CSV file or drag and drop'}
                </p>
              </label>
            </div>
          </div>

          {file && (
            <div className="mb-4">
              <button
                onClick={handleUpload}
                disabled={uploading}
                className="bg-blue-500 hover:bg-blue-600 disabled:bg-gray-400 text-white px-4 py-2 rounded-lg flex items-center gap-2"
              >
                <Upload size={20} />
                {uploading ? 'Uploading...' : 'Upload CSV'}
              </button>
            </div>
          )}

          {result && (
            <div className="mt-4 p-4 border rounded-lg">
              <h3 className="font-semibold mb-2">Upload Results:</h3>
              <div className="grid grid-cols-3 gap-4 mb-2">
                <div className="text-center">
                  <div className="text-2xl font-bold text-blue-600">{result.totalRows}</div>
                  <div className="text-sm text-gray-600">Total Rows</div>
                </div>
                <div className="text-center">
                  <div className="text-2xl font-bold text-green-600">{result.successfulRows}</div>
                  <div className="text-sm text-gray-600">Successful</div>
                </div>
                <div className="text-center">
                  <div className="text-2xl font-bold text-red-600">{result.failedRows}</div>
                  <div className="text-sm text-gray-600">Failed</div>
                </div>
              </div>

              {result.errors && result.errors.length > 0 && (
                <div className="mt-4">
                  <h4 className="font-medium text-red-600 mb-2">Errors:</h4>
                  <div className="max-h-32 overflow-y-auto">
                    {result.errors.map((error, index) => (
                      <div key={index} className="text-sm text-red-600 mb-1">{error}</div>
                    ))}
                  </div>
                </div>
              )}
            </div>
          )}

          <div className="flex justify-end mt-4">
            <button
              onClick={resetModal}
              className="bg-gray-500 hover:bg-gray-600 text-white px-4 py-2 rounded-md"
            >
              Close
            </button>
          </div>
        </div>
      </div>
    );
  };
  export default CsvUploadModal;