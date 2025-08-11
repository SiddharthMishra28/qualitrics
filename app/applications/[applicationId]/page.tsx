
'use client'
import { useState, useEffect } from 'react'
import { useParams } from 'next/navigation'
import { applicationApi, dashboardApi } from '../../lib/api'
import { ApplicationDto, ApplicationExecutionSummary, ExecutionsByApplicationResponse } from '../../types'
import { ArrowLeft, GitBranch, Users, Calendar, ExternalLink } from 'lucide-react'
import Link from 'next/link'
import toast from 'react-hot-toast'

export default function ApplicationDetail() {
  const params = useParams()
  const applicationId = params.applicationId as string
  
  const [application, setApplication] = useState<ApplicationDto | null>(null)
  const [summary, setSummary] = useState<ApplicationExecutionSummary | null>(null)
  const [executions, setExecutions] = useState<ExecutionsByApplicationResponse | null>(null)
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    if (applicationId) {
      loadApplicationData()
    }
  }, [applicationId])

  const loadApplicationData = async () => {
    try {
      setLoading(true)
      const [appData, summaryData, executionsData] = await Promise.all([
        applicationApi.getById(applicationId),
        dashboardApi.getApplicationSummary(applicationId),
        applicationApi.getExecutions(applicationId)
      ])
      setApplication(appData)
      setSummary(summaryData)
      setExecutions(executionsData)
    } catch (error) {
      toast.error('Failed to load application data')
      console.error(error)
    } finally {
      setLoading(false)
    }
  }

  if (loading) {
    return (
      <div className="flex justify-center items-center h-64">
        <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-blue-600"></div>
      </div>
    )
  }

  if (!application) {
    return (
      <div className="text-center py-12">
        <p className="text-gray-500">Application not found</p>
        <Link href="/applications" className="text-blue-600 hover:text-blue-800 mt-4 inline-block">
          ← Back to Applications
        </Link>
      </div>
    )
  }

  return (
    <div className="space-y-6">
      <div className="flex items-center space-x-4">
        <Link
          href="/applications"
          className="text-gray-400 hover:text-gray-600"
        >
          <ArrowLeft size={24} />
        </Link>
        <h1 className="text-3xl font-bold text-gray-900">{application.applicationName}</h1>
      </div>

      {/* Application Info */}
      <div className="card p-6">
        <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
          <div>
            <h2 className="text-lg font-semibold text-gray-900 mb-4">Application Details</h2>
            <div className="space-y-3">
              <div>
                <span className="text-sm text-gray-500">Application ID:</span>
                <p className="font-mono text-sm">{application.applicationId}</p>
              </div>
              <div>
                <span className="text-sm text-gray-500">Description:</span>
                <p className="text-sm">{application.applicationDescription || 'No description provided'}</p>
              </div>
              <div className="flex items-center space-x-4">
                <div className="flex items-center space-x-1">
                  <GitBranch size={16} className="text-gray-400" />
                  <span className="text-sm">{application.stream}</span>
                </div>
                <div className="flex items-center space-x-1">
                  <Users size={16} className="text-gray-400" />
                  <span className="text-sm">{application.crew}</span>
                </div>
              </div>
            </div>
          </div>

          {summary && (
            <div>
              <h2 className="text-lg font-semibold text-gray-900 mb-4">Execution Summary</h2>
              <div className="space-y-3">
                <div className="flex justify-between">
                  <span className="text-sm text-gray-500">Total Builds:</span>
                  <span className="font-semibold">{summary.totalBuilds}</span>
                </div>
                <div className="flex justify-between">
                  <span className="text-sm text-gray-500">Passed:</span>
                  <span className="font-semibold text-green-600">{summary.totalBuildsPassed}</span>
                </div>
                <div className="flex justify-between">
                  <span className="text-sm text-gray-500">Failed:</span>
                  <span className="font-semibold text-red-600">{summary.totalBuildsFailed}</span>
                </div>
                <div className="flex justify-between">
                  <span className="text-sm text-gray-500">Success Rate:</span>
                  <span className="font-semibold">{summary.percentPassed.toFixed(1)}%</span>
                </div>
                <div className="w-full bg-gray-200 rounded-full h-2 mt-2">
                  <div
                    className="bg-green-600 h-2 rounded-full"
                    style={{ width: `${summary.percentPassed}%` }}
                  ></div>
                </div>
              </div>
            </div>
          )}
        </div>
      </div>

      {/* Recent Executions */}
      {executions && (
        <div className="card">
          <div className="px-6 py-4 border-b border-gray-200">
            <h2 className="text-xl font-semibold text-gray-900">Recent Executions</h2>
          </div>
          <div className="overflow-x-auto">
            <table className="min-w-full divide-y divide-gray-200">
              <thead className="bg-gray-50">
                <tr>
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                    Type
                  </th>
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                    Suite Category
                  </th>
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                    Status
                  </th>
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                    Test Cases
                  </th>
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                    Duration
                  </th>
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                    Report
                  </th>
                </tr>
              </thead>
              <tbody className="bg-white divide-y divide-gray-200">
                {executions.executions.map((execution, index) => (
                  <tr key={index} className="hover:bg-gray-50">
                    <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                      {execution.executionType}
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                      {execution.executionSuiteCategory}
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap">
                      <span className={`inline-flex px-2 py-1 text-xs font-semibold rounded-full ${
                        execution.overallBuildStatus === 'PASSED' 
                          ? 'bg-green-100 text-green-800' 
                          : 'bg-red-100 text-red-800'
                      }`}>
                        {execution.overallBuildStatus}
                      </span>
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                      <div className="space-y-1">
                        <div>Total: {execution.totalTestCases}</div>
                        <div className="flex space-x-4 text-xs">
                          <span className="text-green-600">✓ {execution.countPassed}</span>
                          <span className="text-red-600">✗ {execution.countFailed}</span>
                          <span className="text-yellow-600">⊘ {execution.countSkipped}</span>
                        </div>
                      </div>
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                      {execution.executionTime.toFixed(2)}s
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                      {execution.reportLink ? (
                        <a
                          href={execution.reportLink}
                          target="_blank"
                          rel="noopener noreferrer"
                          className="text-blue-600 hover:text-blue-800 flex items-center space-x-1"
                        >
                          <ExternalLink size={14} />
                          <span>View</span>
                        </a>
                      ) : (
                        <span className="text-gray-400">N/A</span>
                      )}
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        </div>
      )}
    </div>
  )
}
