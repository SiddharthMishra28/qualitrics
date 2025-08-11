
'use client'
import { useState } from 'react'
import { ApplicationDto } from '../../types'
import { applicationApi } from '../../lib/api'
import toast from 'react-hot-toast'
import { X } from 'lucide-react'

interface ApplicationFormProps {
  onSubmit: (application: ApplicationDto) => void
  onCancel: () => void
}

export default function ApplicationForm({ onSubmit, onCancel }: ApplicationFormProps) {
  const [formData, setFormData] = useState<Omit<ApplicationDto, 'id'>>({
    applicationId: '',
    applicationName: '',
    applicationDescription: '',
    stream: '',
    crew: '',
  })
  const [loading, setLoading] = useState(false)

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault()
    
    if (!formData.applicationId || !formData.applicationName || !formData.stream || !formData.crew) {
      toast.error('Please fill in all required fields')
      return
    }

    try {
      setLoading(true)
      const response = await applicationApi.register(formData)
      onSubmit(response)
    } catch (error: any) {
      toast.error(error.response?.data?.message || 'Failed to register application')
      console.error(error)
    } finally {
      setLoading(false)
    }
  }

  const handleInputChange = (e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) => {
    const { name, value } = e.target
    setFormData(prev => ({ ...prev, [name]: value }))
  }

  return (
    <div className="p-6">
      <div className="flex items-center justify-between mb-4">
        <h2 className="text-xl font-semibold text-gray-900">Register New Application</h2>
        <button
          onClick={onCancel}
          className="text-gray-400 hover:text-gray-600"
        >
          <X size={24} />
        </button>
      </div>

      <form onSubmit={handleSubmit} className="space-y-4">
        <div>
          <label htmlFor="applicationId" className="block text-sm font-medium text-gray-700 mb-1">
            Application ID *
          </label>
          <input
            type="text"
            id="applicationId"
            name="applicationId"
            required
            maxLength={32}
            className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent"
            value={formData.applicationId}
            onChange={handleInputChange}
          />
        </div>

        <div>
          <label htmlFor="applicationName" className="block text-sm font-medium text-gray-700 mb-1">
            Application Name *
          </label>
          <input
            type="text"
            id="applicationName"
            name="applicationName"
            required
            maxLength={64}
            className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent"
            value={formData.applicationName}
            onChange={handleInputChange}
          />
        </div>

        <div>
          <label htmlFor="applicationDescription" className="block text-sm font-medium text-gray-700 mb-1">
            Description
          </label>
          <textarea
            id="applicationDescription"
            name="applicationDescription"
            rows={3}
            className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent"
            value={formData.applicationDescription}
            onChange={handleInputChange}
          />
        </div>

        <div>
          <label htmlFor="stream" className="block text-sm font-medium text-gray-700 mb-1">
            Stream *
          </label>
          <input
            type="text"
            id="stream"
            name="stream"
            required
            maxLength={64}
            className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent"
            value={formData.stream}
            onChange={handleInputChange}
          />
        </div>

        <div>
          <label htmlFor="crew" className="block text-sm font-medium text-gray-700 mb-1">
            Crew *
          </label>
          <input
            type="text"
            id="crew"
            name="crew"
            required
            maxLength={64}
            className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent"
            value={formData.crew}
            onChange={handleInputChange}
          />
        </div>

        <div className="flex space-x-3 pt-4">
          <button
            type="submit"
            disabled={loading}
            className="flex-1 btn-primary disabled:opacity-50 disabled:cursor-not-allowed"
          >
            {loading ? 'Registering...' : 'Register Application'}
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
