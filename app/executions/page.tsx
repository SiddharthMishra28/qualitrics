
'use client'
import { useState, useEffect } from 'react'
import { applicationApi, executionApi } from '../lib/api'
import { ApplicationDto, ExecutionDto, ResultPublishedDto } from '../types'
import { Plus, TestTube2, Clock, CheckCircle, XCircle, AlertCircle } from 'lucide-react'
import toast from 'react-hot-toast'
import ExecutionForm from './components/ExecutionForm'

export default function Executions() {
  const [applications, setApplications] = useState<ApplicationDto[]>([])
  const [showForm, setShowForm] = useState(false)
  const [recentResults, setRecentResults] = useState<ResultPublishedDto[]>([])
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    loadApplications()
  }, [])

  const loadApplications = async () => {
    try {
      setLoading(true)
      const response = await applicationApi.getAll()
      setApplications(response.applications)
    } catch (error) {
      toast.error('Failed to load applications')
      console.error(error)
    } finally {
      setLoading(false)
    }
  }

  const handleExecutionPublished = (result: ResultPublishedDto) => {
    setRecentResults(prev => [result, ...prev.slice(0, 9)]) // Keep last 10 results
    setShowForm(false)
    if (result.publishResult === 'SUCCESS') {
      toast.success(`Execution results published successfully! Reference: ${result.executionReferenceId}`)
    } else {
      toast.error('Failed to publish execution results')
    }
  }

  if (loading) {
    return (
      <div className="flex justify-center items-center h-64">
        <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-blue-600"></div>
      </div>
    )
  }

  return (
    <div className="space-y-6">
      <div className="flex items-center justify-between">
        <h1 className="text-3xl font-bold text-gray-900">Test Executions</h1>
        <button
          onClick={() => setShowForm(true)}
          className="btn-primary flex items-center space-x-2"
          disabled={applications.length === 0}
        >
          <Plus size={18} />
          <span>Publish Results</span>
        </button>
      </div>

      {applications.length === 0 && (
        <div className="card p-6 text-center">
          <TestTube2 className="mx-auto h-12 w-12 text-gray-400 mb-4" />
          <h3 className="text-lg font-medium text-gray-900 mb-2">No Applications Found</h3>
          <p className="text-gray-500 mb-4">
            You need to register applications before publishing execution results.
          </p>
          <button
            onClick={() => window.location.href = '/applications'}
            className="btn-primary"
          >
            Register Application
          </button>
        </div>
      )}

      {/* Recent Results */}
      {recentResults.length > 0 && (
        <div className="card">
          <div className="px-6 py-4 border-b border-gray-200">
            <h2 className="text-xl font-semibold text-gray-900">Recent Results</h2>
          </div>
          <div className="divide-y divide-gray-200">
            {recentResults.map((result, index) => (
              <div key={index} className="p-4 flex items-center justify-between">
                <div className="flex items-center space-x-3">
                  {result.publishResult === 'SUCCESS' ? (
                    <CheckCircle className="h-5 w-5 text-green-600" />
                  ) : (
                    <XCircle className="h-5 w-5 text-red-600" />
                  )}
                  <div>
                    <p className="text-sm font-medium text-gray-900">
                      {result.publishResult === 'SUCCESS' ? 'Published Successfully' : 'Publication Failed'}
                    </p>
                    <p className="text-sm text-gray-500">
                      Reference: {result.executionReferenceId}
                    </p>
                  </div>
                </div>
                <Clock className="h-4 w-4 text-gray-400" />
              </div>
            ))}
          </div>
        </div>
      )}

      {/* Execution Guidelines */}
      <div className="card p-6">
        <div className="flex items-start space-x-3">
          <AlertCircle className="h-6 w-6 text-blue-600 flex-shrink-0 mt-0.5" />
          <div>
            <h3 className="text-lg font-medium text-gray-900 mb-2">Publishing Guidelines</h3>
            <ul className="text-sm text-gray-600 space-y-1 list-disc list-inside">
              <li>Ensure all test execution data is accurate before publishing</li>
              <li>Execution Type must be either 'functional' or 'regression'</li>
              <li>Suite Category must be either 'sanity' or 'smoke'</li>
              <li>Total test cases should equal the sum of passed, failed, and skipped tests</li>
              <li>Provide a valid report link for detailed test results</li>
            </ul>
          </div>
        </div>
      </div>

      {/* Execution Form Modal */}
      {showForm && (
        <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center p-4 z-50">
          <div className="bg-white rounded-lg max-w-2xl w-full max-h-[90vh] overflow-y-auto">
            <ExecutionForm
              applications={applications}
              onSubmit={handleExecutionPublished}
              onCancel={() => setShowForm(false)}
            />
          </div>
        </div>
      )}
    </div>
  )
}
