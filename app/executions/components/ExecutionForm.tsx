
'use client'
import { useState } from 'react'
import { ExecutionDto, ApplicationDto, ResultPublishedDto } from '../../types'
import { executionApi } from '../../lib/api'
import toast from 'react-hot-toast'
import { X } from 'lucide-react'

interface ExecutionFormProps {
  applications: ApplicationDto[]
  onSubmit: (result: ResultPublishedDto) => void
  onCancel: () => void
}

export default function ExecutionForm({ applications, onSubmit, onCancel }: ExecutionFormProps) {
  const [formData, setFormData] = useState<ExecutionDto>({
    applicationId: '',
    executionType: 'functional',
    executionSuiteCategory: 'sanity',
    totalTestCases: 0,
    countPassed: 0,
    countFailed: 0,
    countSkipped: 0,
    overallBuildStatus: '',
    executionTime: 0,
    reportLink: '',
  })
  const [loading, setLoading] = useState(false)

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault()
    
    if (!formData.applicationId) {
      toast.error('Please select an application')
      return
    }

    const totalCalculated = formData.countPassed + formData.countFailed + formData.countSkipped
    if (totalCalculated !== formData.totalTestCases) {
      toast.error('Total test cases must equal the sum of passed, failed, and skipped tests')
      return
    }

    // Auto-calculate overall build status
    const buildStatus = formData.countFailed === 0 ? 'PASSED' : 'FAILED'
    const executionData = { ...formData, overallBuildStatus: buildStatus }

    try {
      setLoading(true)
      const result = await executionApi.publishResults(executionData)
      onSubmit(result)
    } catch (error: any) {
      toast.error(error.response?.data?.message || 'Failed to publish execution results')
      console.error(error)
    } finally {
      setLoading(false)
    }
  }

  const handleInputChange = (e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement | HTMLTextAreaElement>) => {
    const { name, value } = e.target
    const numericValue = ['totalTestCases', 'countPassed', 'countFailed', 'countSkipped', 'executionTime'].includes(name)
      ? parseFloat(value) || 0
      : value
    
    setFormData(prev => ({ ...prev, [name]: numericValue }))
  }

  const totalCalculated = formData.countPassed + formData.countFailed + formData.countSkipped
  const isValidTotal = totalCalculated === formData.totalTestCases

  return (
    <div className="p-6">
      <div className="flex items-center justify-between mb-6">
        <h2 className="text-xl font-semibold text-gray-900">Publish Execution Results</h2>
        <button
          onClick={onCancel}
          className="text-gray-400 hover:text-gray-600"
        >
          <X size={24} />
        </button>
      </div>

      <form onSubmit={handleSubmit} className="space-y-4">
        <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
          <div>
            <label htmlFor="applicationId" className="block text-sm font-medium text-gray-700 mb-1">
              Application *
            </label>
            <select
              id="applicationId"
              name="applicationId"
              required
              className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent"
              value={formData.applicationId}
              onChange={handleInputChange}
            >
              <option value="">Select an application</option>
              {applications.map((app) => (
                <option key={app.applicationId} value={app.applicationId}>
                  {app.applicationName} ({app.applicationId})
                </option>
              ))}
            </select>
          </div>

          <div>
            <label htmlFor="executionType" className="block text-sm font-medium text-gray-700 mb-1">
              Execution Type *
            </label>
            <select
              id="executionType"
              name="executionType"
              required
              className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent"
              value={formData.executionType}
              onChange={handleInputChange}
            >
              <option value="functional">Functional</option>
              <option value="regression">Regression</option>
            </select>
          </div>

          <div>
            <label htmlFor="executionSuiteCategory" className="block text-sm font-medium text-gray-700 mb-1">
              Suite Category *
            </label>
            <select
              id="executionSuiteCategory"
              name="executionSuiteCategory"
              required
              className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent"
              value={formData.executionSuiteCategory}
              onChange={handleInputChange}
            >
              <option value="sanity">Sanity</option>
              <option value="smoke">Smoke</option>
            </select>
          </div>

          <div>
            <label htmlFor="executionTime" className="block text-sm font-medium text-gray-700 mb-1">
              Execution Time (seconds) *
            </label>
            <input
              type="number"
              id="executionTime"
              name="executionTime"
              required
              min="0"
              step="0.01"
              className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent"
              value={formData.executionTime}
              onChange={handleInputChange}
            />
          </div>
        </div>

        <div className="grid grid-cols-2 md:grid-cols-4 gap-4">
          <div>
            <label htmlFor="totalTestCases" className="block text-sm font-medium text-gray-700 mb-1">
              Total Test Cases *
            </label>
            <input
              type="number"
              id="totalTestCases"
              name="totalTestCases"
              required
              min="1"
              className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent"
              value={formData.totalTestCases}
              onChange={handleInputChange}
            />
          </div>

          <div>
            <label htmlFor="countPassed" className="block text-sm font-medium text-gray-700 mb-1">
              Passed *
            </label>
            <input
              type="number"
              id="countPassed"
              name="countPassed"
              required
              min="0"
              className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent"
              value={formData.countPassed}
              onChange={handleInputChange}
            />
          </div>

          <div>
            <label htmlFor="countFailed" className="block text-sm font-medium text-gray-700 mb-1">
              Failed *
            </label>
            <input
              type="number"
              id="countFailed"
              name="countFailed"
              required
              min="0"
              className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent"
              value={formData.countFailed}
              onChange={handleInputChange}
            />
          </div>

          <div>
            <label htmlFor="countSkipped" className="block text-sm font-medium text-gray-700 mb-1">
              Skipped *
            </label>
            <input
              type="number"
              id="countSkipped"
              name="countSkipped"
              required
              min="0"
              className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent"
              value={formData.countSkipped}
              onChange={handleInputChange}
            />
          </div>
        </div>

        {!isValidTotal && formData.totalTestCases > 0 && (
          <div className="bg-yellow-50 border border-yellow-200 rounded-lg p-3">
            <p className="text-sm text-yellow-700">
              Warning: Total test cases ({formData.totalTestCases}) doesn't match the sum of passed, failed, and skipped tests ({totalCalculated})
            </p>
          </div>
        )}

        <div>
          <label htmlFor="reportLink" className="block text-sm font-medium text-gray-700 mb-1">
            Report Link
          </label>
          <input
            type="url"
            id="reportLink"
            name="reportLink"
            placeholder="https://example.com/test-report"
            className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent"
            value={formData.reportLink}
            onChange={handleInputChange}
          />
        </div>

        <div className="bg-gray-50 rounded-lg p-4">
          <h4 className="text-sm font-medium text-gray-900 mb-2">Summary</h4>
          <div className="grid grid-cols-2 gap-4 text-sm">
            <div>
              <span className="text-gray-500">Build Status:</span>
              <span className={`ml-2 font-medium ${
                formData.countFailed === 0 ? 'text-green-600' : 'text-red-600'
              }`}>
                {formData.countFailed === 0 ? 'PASSED' : 'FAILED'}
              </span>
            </div>
            <div>
              <span className="text-gray-500">Success Rate:</span>
              <span className="ml-2 font-medium">
                {formData.totalTestCases > 0 
                  ? ((formData.countPassed / formData.totalTestCases) * 100).toFixed(1) + '%'
                  : '0%'}
              </span>
            </div>
          </div>
        </div>

        <div className="flex space-x-3 pt-4">
          <button
            type="submit"
            disabled={loading || !isValidTotal || !formData.applicationId}
            className="flex-1 btn-primary disabled:opacity-50 disabled:cursor-not-allowed"
          >
            {loading ? 'Publishing...' : 'Publish Results'}
          </button>
          <button
            type="button"
            onClick={onCancel}
            className="flex-1 btn-secondary"
          >
            Cancel
          </button>
        </div>
      </form>
    </div>
  )
}
