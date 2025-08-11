
'use client'
import Link from 'next/link'
import { ApplicationDto } from '../../types'
import { ExternalLink, Users, GitBranch } from 'lucide-react'

interface ApplicationCardProps {
  application: ApplicationDto
}

export default function ApplicationCard({ application }: ApplicationCardProps) {
  return (
    <div className="card p-6 hover:shadow-lg transition-shadow">
      <div className="flex items-start justify-between mb-4">
        <div>
          <h3 className="text-lg font-semibold text-gray-900 mb-1">
            {application.applicationName}
          </h3>
          <p className="text-sm text-gray-500">{application.applicationId}</p>
        </div>
        <Link
          href={`/applications/${application.applicationId}`}
          className="text-blue-600 hover:text-blue-800"
        >
          <ExternalLink size={20} />
        </Link>
      </div>

      <p className="text-gray-600 mb-4 text-sm line-clamp-3">
        {application.applicationDescription || 'No description provided'}
      </p>

      <div className="flex items-center space-x-4 text-sm">
        <div className="flex items-center space-x-1">
          <GitBranch size={16} className="text-gray-400" />
          <span className="text-gray-600">{application.stream}</span>
        </div>
        <div className="flex items-center space-x-1">
          <Users size={16} className="text-gray-400" />
          <span className="text-gray-600">{application.crew}</span>
        </div>
      </div>

      <div className="mt-4 pt-4 border-t border-gray-200">
        <Link
          href={`/applications/${application.applicationId}/executions`}
          className="text-sm text-blue-600 hover:text-blue-800 font-medium"
        >
          View Executions →
        </Link>
      </div>
    </div>
  )
}
